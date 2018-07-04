package com.springboot.hibernate.caching;

public class Hibernate_Caching {

	/*
	 * What is Hibernate Caching?
	 * Consider if we query a student object with id as '1' 10 times, then if each and every time hibernate goes to DB, query it and then get it then, it will be a performance hit. So hibernate 
	 * will cache the student object with id '1' when it is queried for the first time and then next time it is queried, hibernate will fetch the object from cache instead of fetching it from DB. 
	 * So it provides good performance.
	 * 
	 * How caching is done?
	 * Hibernate supports 3 types of caching as follows,
	 * 1) level-1 cache
	 * 2) level-2 cache
	 * 3) Query cache
	 * 
	 * level-1 cache
	 * -------------
	 * This cache is maintained at session level. each and every time a session is created, hibernate will create a cache for each session. This cache is specific to each session. The level-1 cache
	 * of session1 cannot be accessed from session2 vice versa. 
	 * Whenever we fetch an item from DB through a session, that object will be cached in the level1 cache of that session.
	 * 		Problem - 1
	 * 		-----------
	 * 		Consider there are 2 sessions accessing 'student' object (id=1,name="yatish"). 'session1' and 'session2' will get the object, that object will be cached in the level-1 cache of 
	 * 		the 'session1' and 'session2'. Next consider, if 'session1' changes 'name' of the student object to "Gagan" and saves it to the DB. Now, if 'session2' does a 'get()' of student object
	 * 		with 'id' as '1', then it will fetch the object from 'session2' cache which still have the student object name as "yatish" even though the DB have the object name updated from "yatish"
	 * 		to "Gagan". hence there is in-consitency/stale data.
	 * 		Solution
	 * 		--------
	 * 		Level-2 cache
	 * 		Problem - 2
	 * 		-----------
	 * 		Consider, 'session1' is created and a 'get()' of student object with 'id' as '1' is done. If we close the 'session1', the cache of 'session1' will be deleted. so if we do 'get()' after
	 * 		creating a new session 'session2', then the object will be fetched from the DB, since the cache has been deleted. Hence performance issue, the cache would have kept alive after session
	 * 		delete so that now when 'session2' does 'get()' it could have fetched the object from cache instead of DB.
	 * 		Solution
	 * 		--------
	 * 		Level-2 cache
	 * 
	 * NOTE: level-1 cache will be enabled by default by hibernate. We cannot disable it. Every object fetches by session will be cached in the corresponding session cache.
	 * 
	 * level-2 cache
	 * -------------
	 * This	cache is maintained at sessionFactory level instead of session level. Once the Session Factory is created, then the level-2 cache will be created. This cache will remain same and will 
	 * be accessible by all the sessions created using this sessionFactory. Even, when the session gets created and closed, the level-1 cache of corresponding cache will be created and deleted but
	 * level-2 cache will remain irrespective of sessions create or close. This level-2 cache will be deleted only once the 'SessionFactory' is closed or hibernate application is stopped.
	 * 		Fetching an object for flow with level-2 cache enabled and annotated with @Cacheable
	 * 		-------------------------------------------------------------------------------------
	 * 		1) If object is present in the level-1 cache, then it will returned from there.
	 * 		2) If the object is not present in the level-1 cache, then, it will be checked in the level-2 cache and if it is present there, then it will be fetched from there. If it is not found in
	 * 		   the level-2 cache also, then it will fetch it from the DB and that will be cached into the level-2 cache and then it will be cached in the corresponding session level-1 cache and it
	 * 			will be returned from there.
	 * 
	 * 		Cache Concurrency Strategy
	 * 		--------------------------
	 * 		We know that there is lot of inconsitencies/statle entries that can happen like the 'problem-1' we saw above in level-1 cache. For that issue we can specify what concurrency model to use
	 * 		on each classes with @Cacheable. The types of concurrency strategy are as follow,
	 * 		1) READ_ONLY --> used only for entities that never change (exception is thrown if an attempt to update such an entity is made).
	 * 		2) NONSTRICT_READ_WRITE --> Cache is updated after a transaction that changed the affected data has been committed. Thus, strong consistency is not guaranteed and there is a small time 
	 * 								    window in which stale data may be obtained from cache. This kind of strategy is suitable for use cases that can tolerate eventual consistency.
	 * 		3) READ_WRITE --> This strategy guarantees strong consistency which it achieves by using ‘soft’ locks: When a cached entity is updated, a soft lock is stored in the cache for that 
	 * 						  entity as well, which is released after the transaction is committed. All concurrent transactions that access soft-locked entries will fetch the corresponding data
	 * 						  directly from database.
	 * 		4) TRANSACTIONAL --> Cache changes are done in distributed XA transactions. A change in a cached entity is either committed or rolled back in both database and cache in the same XA 
	 * 							 transaction.
	 * 
	 * 		How to specify Concurrency Strategy
	 * 		-----------------------------------
	 * 		@Entity
			@Cacheable
			@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
			public class Foo {
			........
			}
	 * 			
	 * 
	 * NOTE: level-2 cache will not be enabled by default by hibernate. We have to enable it explicitly/manually by specifying 'hibernate.cache.use_second_level_cache=true' in 
	 * 		 'application.properties' persent 'src/java/resources'
	 * NOTE: For level-2 cache we have to use any of the third-party cache like 'EhCache' or 'Oracle cache' or 'Jboss cache'. we have specify explicitly by specifying 
	 * 		'hibernate.cache.region.factory_class=org.hibernate.cache.ehcache.EhCacheRegionFactory' in 'application.properties' persent 'src/java/resources'
	 * NOTE: By Default, all the objects fetched will not be cached into level-2 cache, we have to explicitly annotate the class with @Cacheable. If level-2 cache is enabled and we are fetching the
	 * 		object without @Cacheable for the first time, then that object will be fetched from DB and it will not be cached in level-2 cache, but it will be cached in the level-1 cache.
	 * NOTE: Even if we specify @Cacheable on a class, the collection properties like List/Set etc will not be cached, only properties which are not collections will be cached. If we want to cache
	 * 		collection properties, then we have to explicitly specify @Cacheable on corresponding collection property. As follows,
	 * 		@Entity
			@Cacheable
			@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
			public class Foo {
			 
			    ...
			 
			    @Cacheable
			    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
			    @OneToMany
			    private Collection<Bar> bars;
			 
			    // getters and setters
			}
	 * 
	 * 
	 */
}
