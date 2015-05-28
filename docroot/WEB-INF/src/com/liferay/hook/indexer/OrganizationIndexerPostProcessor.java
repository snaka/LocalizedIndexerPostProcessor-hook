package com.liferay.hook.indexer;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.model.Organization;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class OrganizationIndexerPostProcessor extends BaseIndexerPostProcessor {
	@Override
	public void postProcessDocument(Document document, Object object)
			throws Exception {

		Organization organization = (Organization) object;

		Long groupId = organization.getGroupId();
		String name = organization.getName();

		Map<Locale, String> nameMap = new HashMap<Locale, String>();

		Locale locales[] = LanguageUtil.getAvailableLocales(groupId);

		for (int i = 0; i < locales.length; i++) {
			nameMap.put(locales[i], name);
		}
		document.addLocalizedText(Field.NAME, nameMap);
	}

	@Override
	public void postProcessSearchQuery(BooleanQuery searchQuery,
			SearchContext searchContext) throws Exception {

		String[] fields = { Field.NAME };
		addSearchTerms(searchQuery, searchContext, fields, false);
	}

}
