package com.searchengine.demo.search.service;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.searchengine.demo.model.Species;
import com.searchengine.demo.search.dto.SearchPage;
import com.searchengine.demo.search.dto.SearchResult;
import com.searchengine.demo.search.model.OwnerSearchDocument;
import com.searchengine.demo.search.model.PetSearchDocument;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
public class SearchService {
	private static final int MAX_PAGE_SIZE = 100;
	private static final long MAX_GLOBAL_RESULT_WINDOW = 10_000;

	private final ElasticsearchOperations elasticsearchOperations;

	public SearchService(ElasticsearchOperations elasticsearchOperations) {
		this.elasticsearchOperations = elasticsearchOperations;
	}

	public SearchPage searchAll(String text, int page, int size) {
		String queryText = requireText(text);
		Pageable requestedPage = pageRequest(page, size);
		long endOfRequestedPage = requestedPage.getOffset() + requestedPage.getPageSize();
		if (endOfRequestedPage > MAX_GLOBAL_RESULT_WINDOW) {
			throw badRequest("Global search supports result windows up to " + MAX_GLOBAL_RESULT_WINDOW);
		}

		// Search each index deeply enough to build the requested combined page.
		Pageable candidatePage = PageRequest.of(0, Math.toIntExact(endOfRequestedPage));
		SearchHits<PetSearchDocument> petHits = elasticsearchOperations.search(
				petQuery(queryText, null, null, candidatePage), PetSearchDocument.class);
		SearchHits<OwnerSearchDocument> ownerHits = elasticsearchOperations.search(
				ownerQuery(queryText, candidatePage), OwnerSearchDocument.class);

		List<SearchResult> candidates = Stream.concat(
				petHits.getSearchHits().stream().map(SearchService::toPetResult),
				ownerHits.getSearchHits().stream().map(SearchService::toOwnerResult))
				.sorted(Comparator.comparingDouble(SearchResult::score).reversed()
						.thenComparing(SearchResult::type)
						.thenComparing(SearchResult::id))
				.toList();

		int fromIndex = Math.toIntExact(requestedPage.getOffset());
		int toIndex = Math.min(fromIndex + requestedPage.getPageSize(), candidates.size());
		List<SearchResult> results = fromIndex >= candidates.size()
				? List.of()
				: new ArrayList<>(candidates.subList(fromIndex, toIndex));

		return new SearchPage(results, petHits.getTotalHits() + ownerHits.getTotalHits(), page, size);
	}

	public SearchPage searchPets(String text, Species species, Long ownerId, int page, int size) {
		Pageable pageable = pageRequest(page, size);
		SearchHits<PetSearchDocument> hits = elasticsearchOperations.search(
				petQuery(text, species, ownerId, pageable), PetSearchDocument.class);

		List<SearchResult> results = hits.getSearchHits().stream()
				.map(SearchService::toPetResult)
				.toList();
		return new SearchPage(results, hits.getTotalHits(), page, size);
	}

	public SearchPage searchOwners(String text, int page, int size) {
		String queryText = requireText(text);
		Pageable pageable = pageRequest(page, size);
		SearchHits<OwnerSearchDocument> hits = elasticsearchOperations.search(
				ownerQuery(queryText, pageable), OwnerSearchDocument.class);

		List<SearchResult> results = hits.getSearchHits().stream()
				.map(SearchService::toOwnerResult)
				.toList();
		return new SearchPage(results, hits.getTotalHits(), page, size);
	}

	private NativeQuery petQuery(String text, Species species, Long ownerId, Pageable pageable) {
		return NativeQuery.builder()
				.withQuery(query -> query.bool(bool -> {
					if (StringUtils.hasText(text)) {
						bool.must(must -> must.multiMatch(match -> match
								.query(text.trim())
								.fields("name^3", "breed", "ownerName")));
					}
					if (species != null) {
						bool.filter(filter -> filter.term(term -> term
								.field("species")
								.value(species.name())));
					}
					if (ownerId != null) {
						bool.filter(filter -> filter.term(term -> term
								.field("ownerId")
								.value(ownerId)));
					}
					if (!StringUtils.hasText(text) && species == null && ownerId == null) {
						bool.must(must -> must.matchAll(matchAll -> matchAll));
					}
					return bool;
				}))
				.withPageable(pageable)
				.build();
	}

	private NativeQuery ownerQuery(String text, Pageable pageable) {
		Query ownerNameQuery = StringUtils.hasText(text)
				? Query.of(query -> query.match(match -> match.field("fullName").query(text.trim())))
				: Query.of(query -> query.matchAll(matchAll -> matchAll));

		return NativeQuery.builder()
				.withQuery(ownerNameQuery)
				.withPageable(pageable)
				.build();
	}

	private Pageable pageRequest(int page, int size) {
		if (page < 0) {
			throw badRequest("Page must be zero or greater");
		}
		if (size < 1 || size > MAX_PAGE_SIZE) {
			throw badRequest("Page size must be between 1 and " + MAX_PAGE_SIZE);
		}
		return PageRequest.of(page, size);
	}

	private String requireText(String text) {
		if (!StringUtils.hasText(text)) {
			throw badRequest("Search text 'q' must not be blank");
		}
		return text.trim();
	}

	private ResponseStatusException badRequest(String message) {
		return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
	}

	private static SearchResult toPetResult(SearchHit<PetSearchDocument> hit) {
		PetSearchDocument pet = hit.getContent();
		String summary = pet.species().name();
		if (StringUtils.hasText(pet.breed())) {
			summary += " · " + pet.breed();
		}
		if (StringUtils.hasText(pet.ownerName())) {
			summary += " · Owner: " + pet.ownerName();
		}
		return new SearchResult("pet", pet.id(), pet.name(), summary, hit.getScore());
	}

	private static SearchResult toOwnerResult(SearchHit<OwnerSearchDocument> hit) {
		OwnerSearchDocument owner = hit.getContent();
		return new SearchResult("owner", owner.id(), owner.fullName(), "Owner", hit.getScore());
	}
}
