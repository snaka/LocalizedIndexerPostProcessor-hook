package com.liferay.hook.indexer;

import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.DocumentImpl;
import com.liferay.portal.kernel.search.IndexerPostProcessor;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.Summary;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.io.Serializable;
import java.util.Locale;

import javax.portlet.PortletURL;

public class BaseIndexerPostProcessor implements IndexerPostProcessor {

	@Override
	public void postProcessContextQuery(BooleanQuery contextQuery,
			SearchContext searchContext) throws Exception {
	}

	@Override
	public void postProcessDocument(Document document, Object obj)
			throws Exception {
	}

	@Override
	public void postProcessFullQuery(BooleanQuery fullQuery,
			SearchContext searchContext) throws Exception {
	}

	@Override
	public void postProcessSearchQuery(BooleanQuery searchQuery,
			SearchContext searchContext) throws Exception {
	}

	@Override
	public void postProcessSummary(Summary summary, Document document,
			Locale locale, String snippet, PortletURL portletURL) {
	}

	public void addSearchTerms(BooleanQuery searchQuery,
			SearchContext searchContext, String[] fields, Boolean like)
			throws Exception {

		for (String field : fields) {
			addSearchTerm(searchQuery, searchContext,
					DocumentImpl.getLocalizedName(searchContext.getLocale(),
							field), like);
		}
	}

	/**
	 * See
	 * {@link com.liferay.portal.kernel.search.BaseIndexer#addSearchTerm(BooleanQuery, SearchContext, String, boolean)}
	 */
	protected void addSearchTerm(BooleanQuery searchQuery,
			SearchContext searchContext, String field, boolean like)
			throws Exception {

		if (Validator.isNull(field)) {
			return;
		}

		String value = null;

		Serializable serializable = searchContext.getAttribute(field);

		if (serializable != null) {
			Class<?> clazz = serializable.getClass();

			if (clazz.isArray()) {
				value = StringUtil.merge((Object[]) serializable);
			} else {
				value = GetterUtil.getString(serializable);
			}
		} else {
			value = GetterUtil.getString(serializable);
		}

		if (Validator.isNotNull(value)
				&& (searchContext.getFacet(field) != null)) {

			return;
		}

		if (Validator.isNull(value)) {
			value = searchContext.getKeywords();
		}

		if (Validator.isNull(value)) {
			return;
		}

		if (searchContext.isAndSearch()) {
			searchQuery.addRequiredTerm(field, value, like);
		} else {
			searchQuery.addTerm(field, value, like);
		}
	}

}
