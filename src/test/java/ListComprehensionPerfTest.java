import org.apache.commons.lang3.tuple.Pair;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * Created by farolfo on 7/19/16.
 */
@Ignore
public class ListComprehensionPerfTest {

    /**
     * Test for { x | x E {1,2,3,4,...} ^ x is even } using List Comprehensions
     */
    @Test
    public void filterLCTest() {
        List<Long> bigList = createBigList();

        long startTime = System.nanoTime();
        List<Long> evens = filter(bigList, (Long x) -> x % 2 == 0);
        long estimatedTime = System.nanoTime() - startTime;

        System.out.println("Time: " + estimatedTime);
    }

    /**
     * Test for { x | x E {1,2,3,4,...} ^ x is even } using filter
     */
    @Test
    public void filterFUNCTest() {
        List<Long> bigList = createBigList();

        long startTime = System.nanoTime();
        List<Long> evens = bigList.stream().filter((Long x) -> x % 2 == 0).collect(Collectors.toList());
        long estimatedTime = System.nanoTime() - startTime;

        System.out.println("Time: " + estimatedTime);
    }

    /**
     * Test for { x | x E {1,2,3,4,...} ^ x is even } using for+if
     */
    @Test
    public void filterFORTest() {
        List<Long> bigList = createBigList();

        long startTime = System.nanoTime();
        List<Long> evens = new ArrayList<>();
        for (int i = 0; i < bigList.size(); i++) {
            if (bigList.get(i) % 2 == 0) {
                evens.add(bigList.get(i));
            }
        }
        long estimatedTime = System.nanoTime() - startTime;
        System.out.println("Time: " + estimatedTime);
    }

    private List<Long> createBigList() {
        return createBigList(100000000);
    }

    private List<Long> createBigList(int cant) {
        List l = new ArrayList<>();
        for (int i = 0; i<cant; i++) {
            l.add(Math.round(Math.random() * 70));
        }
        return l;
    }

    public <T> List<T> filter(List<T> list, Predicate<T> p) {
        return new ListComprehension<T>()
                .suchThat(s -> {
                    s.belongsTo(list);
                    s.holds(p);
                });
    }

    /**
     * Test for { x * 2 | x E {1,2,3,4, ...} } with List Comprehensions
     */
    @Test
    public void mapLCTest() {
        List<Long> bigList = createBigList();

        long startTime = System.nanoTime();
        List<Long> duplicated = map(bigList, (Long x) -> x * 2);
        long estimatedTime = System.nanoTime() - startTime;
        System.out.println("Time: " + estimatedTime);
    }

    /**
     * Test for { x * 2 | x E {1,2,3,4, ...} } with Map
     */
    @Test
    public void mapFUNCTest() {
        List<Long> bigList = createBigList();

        long startTime = System.nanoTime();
        List<Long> duplicated = bigList.stream().map((Long x) -> x * 2).collect(Collectors.toList());
        long estimatedTime = System.nanoTime() - startTime;
        System.out.println("Time: " + estimatedTime);
    }

    /**
     * Test for { x * 2 | x E {1,2,3,4, ...} } with for
     */
    @Test
    public void mapFORTest() {
        List<Long> bigList = createBigList();

        long startTime = System.nanoTime();
        List<Long> duplicated = new ArrayList<>();
        for (int i = 0; i < bigList.size(); i++) {
            duplicated.add(bigList.get(i) * 2);
        }
        long estimatedTime = System.nanoTime() - startTime;
        System.out.println("Time: " + estimatedTime);
    }

    public <T> List<T> map(List<T> list, Function<T,T> f) {
        return new ListComprehension<T>()
                .outputExpression(t -> f.apply(t))
                .suchThat(s -> {
                    s.belongsTo(list);
                });
    }

    /**
     * Test for { (x,y) | x E {1,2, ...} ^ y E {3,4 ,...} } with List Comprehensions
     */
    @Test
    public void cartesianProductLCTest() {
        List<Long> bigList1 = createBigList(10000);
        List<Long> bigList2 = createBigList(10000);

        long startTime = System.nanoTime();
        List<Pair<Long,Long>> cartesianProduct = new ListComprehension<Long>().suchThat((x, y) -> {
            x.belongsTo(bigList1);
            y.belongsTo(bigList2);
        });
        long estimatedTime = System.nanoTime() - startTime;
        System.out.println("Time: " + estimatedTime);
    }

    /**
     * Test for { (x,y) | x E {1,2, ...} ^ y E {3,4, ...} } with For
     */
    @Test
    public void cartesianProductFORTest() {
        List<Long> bigList1 = createBigList(10000);
        List<Long> bigList2 = createBigList(10000);

        long startTime = System.nanoTime();
        List<Pair<Long,Long>> cartesianProduct = new ArrayList<>();
        for(Long x : bigList1) {
            for(Long y : bigList2) {
                cartesianProduct.add(Pair.of(x, y));
            }
        }
        long estimatedTime = System.nanoTime() - startTime;
        System.out.println("Time: " + estimatedTime);
    }

    /**
     * Test for { (x,y) | x E {1,2, ...} ^ y E {3,4, ...} } with Map
     */
    @Test
    public void cartesianProductMAPTest() {
        List<Long> bigList1 = createBigList(10000);
        List<Long> bigList2 = createBigList(10000);

        long startTime = System.nanoTime();
        List<Pair<Long,Long>> cartesianProduct = new ArrayList<>();
        bigList1.stream().map((Long x) -> {
            return bigList2.stream().map((Long y) -> {
                return cartesianProduct.add(Pair.of(x, y));
            });
        });
        long estimatedTime = System.nanoTime() - startTime;
        System.out.println("Time: " + estimatedTime);
    }
}