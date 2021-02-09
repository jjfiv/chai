# Chai 
![CI](https://github.com/jjfiv/chai/workflows/Java%20CI%20with%20Maven/badge.svg) [![Coverage Status](https://coveralls.io/repos/github/jjfiv/chai/badge.svg?branch=master)](https://coveralls.io/github/jjfiv/chai?branch=master)

*Because I don't like coffee, but I like Java. Mostly.*

## About

The Java standard libraries are amazing. When I pull out a ``ConcurrentHashMap``, I know I'm standing on the shoulders of giants. There are other great libraries like [GNU Trove](http://trove.starlight-systems.com/), which add pieces that are missing to the standard libraries (primitive collections) but don't necessarily play nice with the interfaces in the standard JDK.

Another way to put it is that in my work I need the raw performance and I prefer the typing of Java, but I'm really jealous of [Clojure's standard library](http://clojure.github.io/clojure/clojure.core-api.html).

Chai is a lightweight, well-tested, opinionated library that builds on some of the really nice interfaces and tools built into the JDK that makes my coding and prototyping a lot easier and simpler.

## About TL;DR

Everyone has their own library of utility functions. Mine's just better tested than most.

## Version Support

Supports Java 8 and above. I love default methods on interfaces and lambdas in general. I have Github Actions double-checking my commits so that I don't break anything.

## Documentation

This is a tough one. I'm going to figure out how to build and push javadoc somewhere soon, but until then, check out some of the tests for an example of some of the useful static functions provided by Chai.

Until then, some favorites:
```java
// Chai provides wrapper types that allow chaining:
assertEquals(
  Arrays.asList(10, 20, 30),
  ChaiMap.create(
    Pair.of(1, 10),
    Pair.of(2, 20),
    Pair.of(3, 30)
	).readOnly().vals().sorted().intoList());
```

Or if you ever just really need a simple "group-by" operation. Note that this plays nice with existing JVM types, like ``List<T>`` and ``Iterable<T>``.

```java
public static <K,T> Map<K, List<T>> groupBy(Iterable<T> data, TransformFn<T,K> makeKeyFn) {
  Map<K, List<T>> grouped = new HashMap<>();
  for (T t : data) {
    MapFns.extendListInMap(grouped, makeKeyFn.transform(t), t);
  }
  return grouped;
}
```
## Include in Maven

To add the [jitpack.io maven repo](https://jitpack.io/) to your maven repos:
```xml 
 	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```

To add this dependency:
```xml
	<dependency>
	    <groupId>com.github.jjfiv</groupId>
	    <artifactId>chai</artifactId>
	    <version>v0.9</version>
	</dependency>
```

## History

### v0.9
 - SortedReader, MergeSortedReader (for disk-based merge-sorting)
 - ArrayFns.reverse
 - StreamingStats.isEmpty
 - ClassDataDirectory
 - LinesIterable.progressForEach, ListFns.findAll, IntList.from(int...)
 - StrUtil.wordWrap -> List<String> (Not perfect)
 - depend on Galago utility
 - StrUtil.containsLetterOrDigit
 - IntList::last
 - Debouncer.estimateStr(i,total) to choose which estimate to use
 - public check to see if heap is full
 - UserData::hasUserData(Class<?> klazz)
 - CLI
 - ListFns.slice
 - IntList::pushAll(IntList)
 - IntLIst::forEach(TIntProcedure) leaning on trove here
 - process spawning (Spawn)
 - ClassifierInfo::getPostiiveF scores
 - SetFns for trove
 - ListFns now return ArrayList if important
 - IntervalTree<T> from Apache Cassandra
 - FullStats public
 - fast nextBoolean
 - TopKHeap::takeTop(TObjectIntHashMap<T>)
 - HTTPUtil
 - InputContainer::estimateCount
 - StrUtil::replaceAny
 - AUC.computeAP
 - IO.resource now nullable output
 - AUC.compute() from Wikipedia, SciKit Learn
 - Perceptron : Classifier
 - WebServer, JSONAPI support hostname
 - MString mutable string
 - remove .size on IntList
 - SetFns.difference
 - IntList.removeInt(); to avoid boxing and confusion
 - AChaiList::chooseRandomly
 - IntList.shuffle
 - StrUtil.looksLikeInt
 - CLI.readString

### v0.8
 - InputFinder, TarIterator
 - Java v8
 - CircularIntBuffer
 - IntList.encode, decode
 - LinesIterable counts line numbers
 - Directory.Read
 - Nonnull fixes
 - ListFns.slice
 - ShardedTextWriter
 - Directory.ls
 - Directory.isEmpty
 - IterableFns.sortedStreamingGroupBy
 - StrUtil.slice
 - ListFns.lazyMap
 - Debouncer for time-limited messages
 - IntList.push(int)
 - StrUtil.join
 - IntList.sort()
 - LZFCodec
 - MemoryNotifier.pollMemoryUsage
 - TopKHeap.peek() is now @Nullable
 - FixedSlidingWindow<T> for circular buffer
 - ReservoirSampler<T>
 - BitVector
 - IO.spit(bytes, file)
 - ListFns.map
 - StreamFns.hasMoreData() works with PushBackInputStream and InputStreams that have mark() and reset() impl.
 - ListFns.partition(list, 1) faster
 - ReducingIterator, LazyReduceFn
 - IntList.equals, IntList.hashCode faster
 - trove4j wrappers in MapFactory (@Beta)
 - QuickSort.sort(cmp, keys[], values[])
 - StreamingStats::add
 - InputContainer::isParallel
 - DoubleList like IntList, DoubleList.toStats()
 - MapFns.getOrInsert()
 - StrUtil.collapseSpecialMarks()
 - IntRange.intersects(IntRange)
 - ``TopKHeap.getTotalSeen() -> long``
 - TopKHeap.clear
 - ListFns.fill(size, index -> ?)
 - ``StringIntHashMap @Beta``
 - StrUtil.takeBeforeLast, StrUtil.takeAfterLast
 - LinesIterable.slurp
 - NibbleList
 - chai-web JSONAPI, depends on galago:utility
 - InputStreamable.lines isa LinesIterable
 - Weighted<T> in TopKHeap helper object
 - IntList final fixes
 - PMIObject<T>
 - UserData class-based map
 - StrUtil.substr() like ListFns.slice
 - StreamingStats.pushAll(List<Double>)


### v0.5
 - ``StreamStat``
 - ``finalize()`` in Temporary cleanup to find errors
 - ``extendListInMap`` more efficient
 - ``ListFns.getLast(list)``
 - NonNull
 - ``Pair.getBestComparator()``
 - ``StreamFns.readChannel()`` returns a ``ByteBuffer``
 - ``Sample.letters``
 - ``ListFns.repeatUntilAtLeast``

### v0.4
 - Annotations: ``@Beta``, for classes that aren't ready, ``EmergencyUseOnly``, for methods that ought to be private but aren't.
 - Start using findbugs jsr305 ``@Nullable`` and ``@Nonnull``
 - ``IntList.resize()``
 - ``MapFns.extendCollectionInMap()`` takes a ``GenerateFn<Coll>`` rather than a collection, so that it doesn't wastefully allocate on each call even if the collection is not needed.
 - ``Require.that()``, for when you're not sure assertions are on
 - handle some unicode quotes in ``StrUtil``
 - ``long|Pair<Long,T> Timing.milliseconds(Runnable|GenerateFn<T>)``
 - ``ListFns.slice()``
 - ``ListBasedOrderedSet.toList()``
 - ``ListFns.zip()``
 - ``ListFns.unique()``
 - ``MapFns.fromPairs()``
 - ``ListFnsTest`` catchup
 - Add useful constructor to ``ListBasedOrderedSet``, and fix associated bugs.
 - remove newlines from preview, create a ``MemoryNotifier`` class, ``ThreadsafeLazyPtr``, ``ListFns.maxBy``
 - Fix/Break ``Pair`` comparators to not super ``Map.Entry`` anymore.
 - ``ZipWriter instanceof Flushable``
 - ``QuickSort`` implementation which avoids copying, has wildcarded comparators.
 - ``Builder<T>`` marker interface for things with ``T getOutput()``
 - ``TemporaryDirectory instanceof Directory, GenerateFn<File>``
 - ``IO.openOutputStream(File|String)``
 - ``IOTest``
 - better error message if you try to use a default comparator on something that isn't comparable.
 - ``wrapInputStream`` paving the way for compressed files inside archives, for instance
 - add a quick ``FS.isDirectory`` for when you don't care about the file object afterward.
 - ``IO.resource(String name)``, ``IO.resourceStream(String name)``, and ``IO.slurp(InputStream)``
 - ``StrUtil.indent(text, tabChars)``


### v0.3
 - ``TemporaryDirectory``
 - ``ZipWriter``
 - ``IntMath::fromLong``
 - ``Closer<T> implements GenerateFn<T> { T get(); }``
 - ``Comparing.byteArrays()``
 - ``ArrayFns.compare(byte[], byte[])``
 - Scattered Javadocs and Tests

### v0.2
 - ``StreamFns``
    - ``readBytes``
    - ``fromByteBuffer``
    - ``readAll``
 - More reader factories on ``IO``
 - ``DoubleFns.equals(a,b,epsilon)``
 - ``ZipArchive, ZipArchiveEntry``
 - ``LazyPtr<T>``
 - ``ListFns.partition`` and ``ListFns.roundRobinPartition``
 - ``MapFns.getOrElse``
 - indenting, tests, javadoc

### v0.1
 - XML as a separate module
 - ``TreeFns``
 - ``AChaiList`` is a superset of ``AbstractList`` functionality
 - ``ChaiIterable`` isa ``Collection``
 - ``TopKHeap`` has ``addAll`` and ``bool offer()``
 - Move to github and setup travis
 - ... implement and pull in from other files.

