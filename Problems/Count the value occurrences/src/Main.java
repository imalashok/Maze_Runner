class Counter {

    public static boolean checkTheSameNumberOfTimes(int elem, List<Integer> list1, List<Integer> list2) {
        int count = 0;

        for (Integer list1Elem : list1) {
            if (list1Elem == elem) {
                count++;
            }
        }

        for (Integer list2Elem : list2) {
            if (list2Elem == elem) {
                count--;
            }
        }

        return count == 0;
    }
}