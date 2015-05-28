package com.liferay.hook.indexer;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portlet.blogs.model.BlogsEntry;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BlogsIndexerPostProcessor extends BaseIndexerPostProcessor {
	
	@Override
	public void postProcessDocument(Document document, Object object)
			throws Exception {

		BlogsEntry blogsEntry = (BlogsEntry) object;
		
		Long groupId = blogsEntry.getGroupId();
		String title = blogsEntry.getTitle();
		String content = HtmlUtil.extractText(blogsEntry.getContent());
		String discription = blogsEntry.getDescription();

		Map<Locale, String> contentsMap = new HashMap<Locale, String>();
		Map<Locale, String> titleMap = new HashMap<Locale, String>();
		Map<Locale, String> discriptionMap = new HashMap<Locale, String>();

		Locale locales[] = LanguageUtil.getAvailableLocales(groupId);

		if (content.length() <= 0) {
			return;
		}

		for (int i = 0; i < locales.length; i++) {
			contentsMap.put(locales[i], content);
			titleMap.put(locales[i], title);
			
			if(discription.length() > 0 ) {
				discriptionMap.put(locales[i], discription);
			}
		}
		document.addLocalizedText(Field.TITLE, titleMap);
		document.addLocalizedText(Field.CONTENT, contentsMap);
		document.addLocalizedText(Field.DESCRIPTION, discriptionMap);
	}

	@Override
	public void postProcessSearchQuery(BooleanQuery searchQuery,
			SearchContext searchContext) throws Exception {

		String[] fields = { Field.CONTENT, Field.TITLE, Field.DESCRIPTION };
		addSearchTerms(searchQuery, searchContext, fields, false);
	}
	
}
