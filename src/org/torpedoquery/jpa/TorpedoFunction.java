package org.torpedoquery.jpa;

import static org.torpedoquery.jpa.internal.TorpedoMagic.getTorpedoMethodHandler;
import static org.torpedoquery.jpa.internal.TorpedoMagic.setQuery;

import org.torpedoquery.jpa.internal.Proxy;
import org.torpedoquery.jpa.internal.Selector;
import org.torpedoquery.jpa.internal.functions.CoalesceFunction;
import org.torpedoquery.jpa.internal.handlers.ArrayCallHandler;
import org.torpedoquery.jpa.internal.handlers.AscFunctionHandler;
import org.torpedoquery.jpa.internal.handlers.AvgFunctionHandler;
import org.torpedoquery.jpa.internal.handlers.ComparableConstantFunctionHandler;
import org.torpedoquery.jpa.internal.handlers.ConstantFunctionHandler;
import org.torpedoquery.jpa.internal.handlers.CountFunctionHandler;
import org.torpedoquery.jpa.internal.handlers.CustomFunctionHandler;
import org.torpedoquery.jpa.internal.handlers.DescFunctionHandler;
import org.torpedoquery.jpa.internal.handlers.DistinctFunctionHandler;
import org.torpedoquery.jpa.internal.handlers.IndexFunctionHandler;
import org.torpedoquery.jpa.internal.handlers.MathOperationHandler;
import org.torpedoquery.jpa.internal.handlers.MaxFunctionHandler;
import org.torpedoquery.jpa.internal.handlers.MinFunctionHandler;
import org.torpedoquery.jpa.internal.handlers.SumFunctionHandler;
import org.torpedoquery.jpa.internal.handlers.ValueHandler;
import org.torpedoquery.jpa.internal.query.QueryBuilder;

public class TorpedoFunction {
	
		// JPA Functions
		public static Function<Long> count(Object object) {
			if (object instanceof Proxy) {
				setQuery((Proxy) object);
			}
			return getTorpedoMethodHandler().handle(new CountFunctionHandler(object instanceof Proxy ? (Proxy) object : null));
		}

		public static <V, T extends Comparable<V>> ComparableFunction<V> sum(T number) {
			return getTorpedoMethodHandler().handle(new SumFunctionHandler<V>(number));
		}
		
		public static <V, T extends Comparable<V>> ComparableFunction<V> sum(Function<T> number) {
			return getTorpedoMethodHandler().handle(new SumFunctionHandler<V>(number));
		}

		public static <V, T extends Comparable<V>> ComparableFunction<V> min(T number) {
			return getTorpedoMethodHandler().handle(new MinFunctionHandler<V>(number));
		}
		
		public static <V, T extends Comparable<V>> ComparableFunction<V> min(Function<T> number) {
			return getTorpedoMethodHandler().handle(new MinFunctionHandler<V>(number));
		}

		public static <V, T extends Comparable<V>> ComparableFunction<V> max(T number) {
			return getTorpedoMethodHandler().handle(new MaxFunctionHandler<V>(number));
		}

		public static <V, T extends Comparable<V>> ComparableFunction<V> max(Function<T> number) {
			return getTorpedoMethodHandler().handle(new MaxFunctionHandler<V>(number));
		}
		
		public static <V, T extends Comparable<V>> ComparableFunction<V> avg(T number) {
			return getTorpedoMethodHandler().handle(new AvgFunctionHandler<V>(number));
		}
		
		public static <V, T extends Comparable<V>> ComparableFunction<V> avg(Function<T> number) {
			return getTorpedoMethodHandler().handle(new AvgFunctionHandler<V>(number));
		}

		public static <T, E extends Function<T>> E coalesce(E... values) {
			CoalesceFunction<E> coalesceFunction = getCoalesceFunction(values);
			return (E) coalesceFunction;
		}

		public static <T> Function<T> coalesce(T... values) {
			final CoalesceFunction<T> coalesceFunction = getCoalesceFunction(values);
			return coalesceFunction;
		}

		private static <T> CoalesceFunction<T> getCoalesceFunction(T... values) {
			final CoalesceFunction coalesceFunction = new CoalesceFunction();
			getTorpedoMethodHandler().handle(new ArrayCallHandler(new ValueHandler<Void>() {
				@Override
				public Void handle(Proxy proxy, QueryBuilder queryBuilder, Selector selector) {
					coalesceFunction.setQuery(proxy);
					coalesceFunction.addSelector(selector);
					return null;
				}
			}, values));
			return coalesceFunction;
		}

		public static <T> Function<T> distinct(T object) {
			if (object instanceof Proxy) {
				setQuery((Proxy) object);
			}
			return getTorpedoMethodHandler().handle(new DistinctFunctionHandler<T>(object));
		}

		public static <T> Function<T> constant(T constant) {
			return getTorpedoMethodHandler().handle(new ConstantFunctionHandler<T>(constant));
		}

		public static <V, T extends Comparable<V>> ComparableFunction<T> constant(T constant) {
			return getTorpedoMethodHandler().handle(new ComparableConstantFunctionHandler<T>(constant));
		}

		public static <T> ComparableFunction<Integer> index(T object) {
			if (object instanceof Proxy) {
				setQuery((Proxy) object);
			}
			return getTorpedoMethodHandler().handle(new IndexFunctionHandler(object));
		}
		
		/**
		 *  Use this method to call functions witch are not supported natively by Torpedo
		 *   
		 * @return your custom function
		 */
		public static <T> Function<T> function(String name,Class<T> returnType,Object value) 
		{
			return getTorpedoMethodHandler().handle(new CustomFunctionHandler<T>(name,value));
		}
		
		// orderBy function
		public static <T> Function<T> asc(T object) {
			return getTorpedoMethodHandler().handle(new AscFunctionHandler<T>());
		}

		public static <T> Function<T> desc(T object) {
			return getTorpedoMethodHandler().handle(new DescFunctionHandler<T>());
		}
		
		//math operation
		public static <T> OnGoingMathOperation<T> operation(T left)
		{
			return getTorpedoMethodHandler().handle(new MathOperationHandler<T>(null));
		}
		
		public static <T> OnGoingMathOperation<T> operation(Function<T> left)
		{
			return getTorpedoMethodHandler().handle(new MathOperationHandler<T>(left));
		}
}
