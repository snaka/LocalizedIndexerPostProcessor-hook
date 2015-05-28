package com.liferay.hook.indexer;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.model.Contact;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ContactIndexerPostProcessor extends BaseIndexerPostProcessor {

	@Override
	public void postProcessDocument(Document document, Object object)
			throws Exception {

		Contact contact = (Contact) object;

		if (contact.isUser()) {
			User user = UserLocalServiceUtil.getUserByContactId(contact
					.getContactId());

			if ((user == null) || user.isDefaultUser()
					|| (user.getStatus() != WorkflowConstants.STATUS_APPROVED)) {

				return;
			}
		}
		
		User user = UserLocalServiceUtil.getUser(contact.getUserId());
		Long groupId = user.getGroupId();

		String emailAddress = contact.getEmailAddress();
		String firstName = contact.getFirstName();
		String fullName = contact.getFullName();
		String jobTitle = contact.getJobTitle();
		String lastName = contact.getLastName();
		String middleName = contact.getMiddleName();

		Map<Locale, String> emailAddressMap = new HashMap<Locale, String>();
		Map<Locale, String> firstNameMap = new HashMap<Locale, String>();
		Map<Locale, String> fullNameMap = new HashMap<Locale, String>();
		Map<Locale, String> jobTitleMap = new HashMap<Locale, String>();
		Map<Locale, String> lastNameMap = new HashMap<Locale, String>();
		Map<Locale, String> middleNameMap = new HashMap<Locale, String>();

		Locale locales[] = LanguageUtil.getAvailableLocales(groupId);

		for (int i = 0; i < locales.length; i++) {
			emailAddressMap.put(locales[i], emailAddress);
			firstNameMap.put(locales[i], firstName);
			fullNameMap.put(locales[i], fullName);
			jobTitleMap.put(locales[i], jobTitle);
			lastNameMap.put(locales[i], lastName);
			middleNameMap.put(locales[i], middleName);
		}

		document.addLocalizedText("emailAddress", emailAddressMap);
		document.addLocalizedText("firstName", firstNameMap);
		document.addLocalizedText("fullName", fullNameMap);
		document.addLocalizedText("jobTitle", jobTitleMap);
		document.addLocalizedText("lastName", lastNameMap);
		document.addLocalizedText("middleName", middleNameMap);
	}

	@Override
	public void postProcessSearchQuery(BooleanQuery searchQuery,
			SearchContext searchContext) throws Exception {

		String[] fields = { "emailAddress", "firstName", "fullName",
				"jobTitle", "lastName", "middleName" };
		addSearchTerms(searchQuery, searchContext, fields, false);
	}
}
