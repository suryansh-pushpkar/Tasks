

public class Stream{
    public static void main(String[] args) {
        List<Integer> nums  = Arrays.asList(4,5,6,7,8,9);
        Stream<Integer> data = nums.stream();
        data.forEach(n->System.out.println(n));
    }
}