package com.liferay.hook.indexer;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portlet.bookmarks.model.BookmarksEntry;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BookmarksEntryIndexerPostProcessor extends
		BaseIndexerPostProcessor {

	@Override
	public void postProcessDocument(Document document, Object object)
			throws Exception {

		BookmarksEntry bookMarkEntry = (BookmarksEntry) object;

		Long groupId = bookMarkEntry.getGroupId();
		String title = bookMarkEntry.getName();
		String description = bookMarkEntry.getDescription();
		String url = bookMarkEntry.getUrl();

		Map<Locale, String> titleMap = new HashMap<Locale, String>();
		Map<Locale, String> descriptionMap = new HashMap<Locale, String>();
		Map<Locale, String> urlMap = new HashMap<Locale, String>();

		Locale locales[] = LanguageUtil.getAvailableLocales(groupId);

		for (int i = 0; i < locales.length; i++) {
			titleMap.put(locales[i], title);
			descriptionMap.put(locales[i], description);
			urlMap.put(locales[i], url);
		}
		document.addLocalizedText(Field.TITLE, titleMap);
		document.addLocalizedText(Field.DESCRIPTION, descriptionMap);
		document.addLocalizedText(Field.URL, urlMap);
	}

	@Override
	public void postProcessSearchQuery(BooleanQuery searchQuery,
			SearchContext searchContext) throws Exception {

		String[] fields = { Field.TITLE, Field.DESCRIPTION, Field.URL };
		addSearchTerms(searchQuery, searchContext, fields, false);
	}

}
