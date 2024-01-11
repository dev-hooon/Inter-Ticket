package dev.hooon.common.dto;

import java.util.List;

public record PagedResponse<T>(
	List<T> content,
	int totalPages,
	int totalItems,
	int currentPage,
	int pageSize
) {
}
