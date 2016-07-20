import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

@SuppressWarnings("unchecked")
public class ListComprehensionTest {

    /**
     * Test for { x | x E {1,2,3,4} ^ x is even }
     */
    @Test
    public void filterTest() {
        List<Integer> someIntegers = Arrays.asList(1, 2, 3, 4);
        List<Integer> actualEvens = Arrays.asList(2, 4);

        List<Integer> evens = filter(someIntegers, (Integer x) -> x % 2 == 0);

        assertThat(evens, is(actualEvens));
    }

    public <T> List<T> filter(List<T> list, Predicate<T> p) {
        return new ListComprehension<T>()
                .suchThat(s -> {
                    s.belongsTo(list);
                    s.holds(p);
                });
    }

    /**
     * Test for { x * 2 | x E {1,2,3,4} }
     */
    @Test
    public void mapTest() {
        List<Integer> someIntegers = Arrays.asList(1, 2, 3, 4);
        List<Integer> actualDuplicated = Arrays.asList(2, 4, 6, 8);

        List<Integer> duplicated = map(someIntegers, (Integer x) -> x * 2);

        assertThat(duplicated, is(actualDuplicated));
    }

    public <T> List<T> map(List<T> list, Function<T,T> f) {
        return new ListComprehension<T>()
                .outputExpression(t -> f.apply(t))
                .suchThat(s -> {
                    s.belongsTo(list);
                });
    }

    /**
     * Test for { (x,y) | x E {1,2} ^ y E {3,4} }
     */
    @Test
    public void cartesianProductTest() {
        List<Integer> someIntegers = Arrays.asList(1, 2);
        List<Integer> moreIntegers = Arrays.asList(3, 4);
        List<Pair<Integer,Integer>> actualCartesianProduct = Arrays.asList(Pair.of(1,3), Pair.of(1,4),
                Pair.of(2,3), Pair.of(2,4));

        List<Pair<Integer,Integer>> cartesianProduct = new ListComprehension<Integer>().suchThat((x, y) -> {
            x.belongsTo(someIntegers);
            y.belongsTo(moreIntegers);
        });

        assertThat(cartesianProduct, is(actualCartesianProduct));
    }

    /**
     * Test for { (x,y) | x E {1,2,3} ^ y E {4,5,6} ^ x * 2 = y }
     */
    @Test
    public void relationBetweenXYTest() {
        List<Integer> someIntegers = Arrays.asList(1, 2, 3);
        List<Integer> moreIntegers = Arrays.asList(4, 5, 6);
        List<Pair<Integer,Integer>> actualDoublePairs = Arrays.asList(Pair.of(2,4), Pair.of(3,6));

        BiPredicate<Integer, Integer> condition = (x, y) -> x * 2 == y;

        List<Pair<Integer,Integer>> doublePairs = new ListComprehension<Integer>().suchThat((x, y) -> {
            x.belongsTo(someIntegers);
            y.belongsTo(moreIntegers);
            x.holds(condition);
        });

        assertThat(doublePairs, is(actualDoublePairs));
    }

}