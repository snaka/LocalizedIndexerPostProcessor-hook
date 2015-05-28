/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.hook.indexer;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portlet.messageboards.model.MBMessage;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MBMessageIndexerPostProcessor extends BaseIndexerPostProcessor {

	@Override
	public void postProcessDocument(Document document, Object object)
			throws Exception {

		MBMessage mbMessage = (MBMessage) object;

		Map<Locale, String> contentsMap = new HashMap<Locale, String>();
		Map<Locale, String> titleMap = new HashMap<Locale, String>();

		// Get all avilable languages of this site to create indexs for all of
		// them.
		//
		// To get Site Language, use LocaleThreadLocal.getSiteDefaultLocale()
		// To get Display language, use
		// LocaleThreadLocal.getThemeDisplayLocale()
		Long groupId = mbMessage.getGroupId();
		Locale locales[] = LanguageUtil.getAvailableLocales(groupId);

		String content = document.get(Field.CONTENT);
		String title = document.get(Field.TITLE);

		if (content.length() <= 0) {
			return;
		}

		for (int i = 0; i < locales.length; i++) {
			contentsMap.put(locales[i], content);
			titleMap.put(locales[i], title);
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