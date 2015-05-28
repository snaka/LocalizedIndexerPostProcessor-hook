package com.liferay.hook.indexer;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portlet.bookmarks.model.BookmarksFolder;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BookmarksFolderIndexerPostProcessor extends
		BaseIndexerPostProcessor {

	@Override
	public void postProcessDocument(Document document, Object object)
			throws Exception {

		BookmarksFolder bookmarkFolder = (BookmarksFolder) object;

		Long groupId = bookmarkFolder.getGroupId();
		String title = bookmarkFolder.getName();
		String description = bookmarkFolder.getDescription();

		Map<Locale, String> titleMap = new HashMap<Locale, String>();
		Map<Locale, String> descriptionMap = new HashMap<Locale, String>();

		Locale locales[] = LanguageUtil.getAvailableLocales(groupId);

		for (int i = 0; i < locales.length; i++) {
			titleMap.put(locales[i], title);
			if(description.length() > 0) {
				descriptionMap.put(locales[i], description);
			}
		}
		document.addLocalizedText(Field.TITLE, titleMap);
		document.addLocalizedText(Field.DESCRIPTION, descriptionMap);
	}

	@Override
	public void postProcessSearchQuery(BooleanQuery searchQuery,
			SearchContext searchContext) throws Exception {

		String[] fields = { Field.TITLE, Field.DESCRIPTION };
		addSearchTerms(searchQuery, searchContext, fields, false);
	}
}
