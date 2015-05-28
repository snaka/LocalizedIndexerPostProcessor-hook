package com.liferay.hook.indexer;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portlet.wiki.model.WikiPage;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class WikiPageIndexerPostProcessor extends BaseIndexerPostProcessor {

	@Override
	public void postProcessDocument(Document document, Object object)
			throws Exception {

		WikiPage wikiPage = (WikiPage) object;
		
		Long groupId = wikiPage.getGroupId();
		String title = wikiPage.getTitle();
		String contents = wikiPage.getContent();
		
		Map<Locale, String> titleMap = new HashMap<Locale, String>();
		Map<Locale, String> contentsMap = new HashMap<Locale, String>();

		Locale locales[] = LanguageUtil.getAvailableLocales(groupId);

		for (int i = 0; i < locales.length; i++) {
			titleMap.put(locales[i], title);
			contentsMap.put(locales[i], contents);
		}
		document.addLocalizedText(Field.TITLE, titleMap);
		document.addLocalizedText(Field.CONTENT, contentsMap);
	}

	@Override
	public void postProcessSearchQuery(BooleanQuery searchQuery,
			SearchContext searchContext) throws Exception {

		String[] fields = { Field.CONTENT, Field.TITLE };
		addSearchTerms(searchQuery, searchContext, fields, false);
		
	}
	
}
