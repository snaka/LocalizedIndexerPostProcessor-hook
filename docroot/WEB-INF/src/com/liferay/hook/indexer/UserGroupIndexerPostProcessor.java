package com.liferay.hook.indexer;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.model.UserGroup;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class UserGroupIndexerPostProcessor extends BaseIndexerPostProcessor {
	@Override
	public void postProcessDocument(Document document, Object object)
			throws Exception {

		UserGroup userGroup = (UserGroup) object;

		Long groupId = userGroup.getGroupId();
		String name = userGroup.getName();
		String discription = userGroup.getDescription();

		Map<Locale, String> namesMap = new HashMap<Locale, String>();
		Map<Locale, String> discriptionMap = new HashMap<Locale, String>();

		Locale locales[] = LanguageUtil.getAvailableLocales(groupId);

		for (int i = 0; i < locales.length; i++) {
			namesMap.put(locales[i], name);
			
			if(discription.length() > 0 ) {
				discriptionMap.put(locales[i], discription);
			}
		}
		document.addLocalizedText(Field.NAME, namesMap);
		document.addLocalizedText(Field.DESCRIPTION, discriptionMap);
	}

	@Override
	public void postProcessSearchQuery(BooleanQuery searchQuery,
			SearchContext searchContext) throws Exception {

		String[] fields = { Field.NAME, Field.DESCRIPTION };
		addSearchTerms(searchQuery, searchContext, fields, false);
	}
}
