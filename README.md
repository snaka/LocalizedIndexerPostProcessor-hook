#LocalizedIndexerPostProcessor-hook
LocalizedIndexerPostProcessor-hookis a PoC (Proof of a Concept) for adding localized indexes for Liferay6.2 + Solr 4.3.1 (or higher version) + Solr4 plugin combination.  

##Summary
In Liferay 6.2, Web Content have dedicated index fields for each language, however the following portlets do not have localized index fields.
- Messageboard,
- Blogs
- Wiki
- Bookmarks
- Contact
- Organization
- UserGroup

Because there are no localized index fields, the Search Portlet doesn't work with multi byte languages such as Japanese, Korean and Chinese out of box for these portlets.

LocalizedIndexerPostProcessor-hook is a way to create a fairly appropriate localized fields for languages available with Japanese, Korean, and Chinese characters and words. 

Also Liferay 6.2 uses Lucene 3.5 which uses CJKAnalyzer for Japanese / Chinese / Korean tokenizer. Unfortunately, this tokenizer doesn't really meet the quality of practical use and it's not easily configurable. I strongly recommend to use external search engines such as Solr for Japanese.

In terms of using Solr 4, Liferay provides Solr 4 plugin to integrate Solr and Liferay easily.  Liferay 6.2 officially supports 4.3.1 and higher.

##How to install
The steps to install Solr4 plugin and integrate Solr and Liferay (assuming you've already installed reqired Java Runtime, Java SDK, e.g.)

-  Start Liferay 6.2 server
-  Install Solr4 plugin from Marketplace in the control panel.
-  Download Solr4.3.1 (http://archive.apache.org/dist/lucene/solr/4.3.1/solr-4.3.1.tgz)
-  Expand solr-4.3.1.tgz and copy solrconfig.xml and schema.xml from Solr4 plugin into solr-4.3.1/example/solr/collection1/conf
- 	In solr4-web/WEB-INF/classes/META-INF/solr-spring.xml
```
<bean id="com.liferay.portal.search.solr.server.BasicAuthSolrServer" class="com.liferay.portal.search.solr.server.BasicAuthSolrServer">
    <property name="defaultMaxConnectionsPerRoute" value="20" />
	    <property name="httpRequestInterceptors">
		    <list>
			    <bean class="com.liferay.portal.search.solr.interceptor.PreemptiveAuthInterceptor" />
		    </list>
        </property>
        <property name="maxTotalConnections" value="20" />
        <property name="url" value="XXXX" />
    </property>
</bean>
```
Replace XXXX to your Solr sever address, e.g. http://localhost:8983/solr

- Stop Liferay 
- Start Solr 
/solr-4.3.1/example$ java -jar start.jar
-  Restart Liferay 
-  Download this hook, compiled with appropriate SDK compatible with Liferay version that you used and deploy the hook to the server.
- 	After deploy finished, navigate to control panel -> Server Administration and run both Reindex all search indexes and Reindex all spell check indexes.

Now you are good to go.

For Japanese tokenizer, Solr4 uses JapaneseTokenizerFactory as the default, which utilize Kuromoji for the morphological analysis. NGramTokenizerFactory is also available as well for Bi-gram indexing.  For more details, please consult to the Solr4.3.1 manual.





