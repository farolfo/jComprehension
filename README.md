![jComprehension](/logo.png)

Build lists in mathematical set-builder notation with Java, like { x * 2 | x E {1,2,3,4} ^ x is even }.

### Introduction

We may solve our problems using `for`+`if` statements in an imperative way, or by the usage of `map`+`filter` if we want to use a functional approach, but we have another powerful way of solving these same problems: list comprehensions. List comprehensions are syntatic constructions that are used to build lists in many languages, such as Haskell, Python or Ruby, using the algebraic set-builder notation. This notation hasn't yet been added to the Java world, and is the intention of this library to do that.

For example, let's suppose we want to get all the even numbers of a given list. In the algebra world we would so with the set-builder notation like
```
{ x | x E R ^ x is-even }
```
In Java we may do
```java
List<Integer> evens = new ArrayList<>();
for (int n : list) {
  if (n % 2 == 0) {
    evens.add(n);
  }
}
```
or
```java
Predicate<Integer> isEven = x -> x % 2 == 0;
List<Integer> evens = list.stream().filter(isEven).collect(Collectors.toList());
```
These two approaches solve our problem, but now we can also do this with the List Comprehensions approach of jComprehension
```java
List<Integer> evens = new ListComprehension<Integer>()
  .suchThat(x -> {
    x.belongsTo(list);
    x.holds(isEven);
});
```

### API

In order to build a List as a List Comprehension you must instantiate a new `ListComprehension` object and call the `suchThat` method with the predicates you want the list to hold.

The `ListComprehension` object exposes the following methods:

* `List<T> suchThat(Consumer<Var> predicates)`: Builds the list of elements with the given predicates. See below to check the methods the `Var` object exposes.
* `List<Pair<T, T>> suchThat(BiConsumer<Var, Var> predicates)`: Builds the list of pairs of elements with the given predicates. See below to check the methods the `Var` object exposes.
* `ListComprehension<T> outputExpression(Function<T, T> resultTransformer)`: Sets an output expressions that will be applied to each element of the resultant list after validating the predicates.
* `ListComprehension<T> outputExpression(BiFunction<T, T, ?> resultTransformer)`: Sets an output expressios that will be applied to each pair of elements of the resultant list after validating the predicates.

When defining the predicates, we have to use a `Var` object in a lambda (`Predicate` or `BiPredicate`).
The `Var` object exposes the following methods:

* `Var belongsTo(List<T> c)`: Defines the list where the elements belong to.
* `Var holds(Predicate<T> p)`: Defines a predicate that each element of the resultant list should hold.
* `Var is(Predicate<T> p)`: Alias of the `Var holds(Predicate<T> p)` method.
* `Var holds(BiPredicate<T, T> p)`: Defines a predicate that each pair of elements of the resultant list should hold.
* `Var is(BiPredicate<T, T> p)`: Alias of the `Var holds(BiPredicate<T, T> p)` method.

### Examples

Filter a list of even numbers:
```java
// { x | x E {1,2,3,4,5} ^ x is-even }

Predicate<Integer> isEven = x -> x % 2 == 0;
List<Integer> evens = new ListComprehension<Integer>()
  .suchThat(s -> {
    s.belongsTo(Arrays.asList(1,2,3,4,5));
    s.holds(isEven);
  });
```

Map a list of numbers in order to have them duplicated:
```java
// { x*2 | x E {1,2,3,4,5} }

List<Integer> duplicated = new ListComprehension<Integer>()
  .outputExpression((Integer x) -> x * 2)
  .suchThat(s -> {
    s.belongsTo(Arrays.asList(1,2,3,4,5));
});
```

Make the cartesian product of two lists and keep the pair of elements that hold that an element is the 2 times the other one: 
```java
// { (x,y) | x E {1,2,3} ^ y E {4,5,6} ^ 2 * x = y }

List<Pair<Integer,Integer>> doublePairs = new ListComprehension<Integer>()
  .suchThat((x, y) -> {
    x.belongsTo(Arrays.asList(1,2,3));
    y.belongsTo(Arrays.asList(4,5,6));
    x.holds((x, y) -> x * 2 == y);
});
```

### Motivation
My college project of Functional Programming at ITBA (Buenos Aires Institute of Technology): https://github.com/farolfo/list-comprehensions-in-java

### Further reading

* [Set-builder notation.](https://en.wikipedia.org/wiki/Set-builder_notation)
* [How are programming languages supporting this today?](https://en.wikipedia.org/wiki/Comparison_of_programming_languages_(list_comprehension))
* [Haskell's list comprehension](http://learnyouahaskell.com/starting-out#im-a-list-comprehension)

### License

MIT
