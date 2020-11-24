package com.song.sakura.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

/**
 * https://leetcode-cn.com/problemset/lcof/
 */
@SuppressWarnings("unused")
class Solution {

    /**
     * LinkedList插入元素，并升序排列
     */

    private void linkedListSort() {
        LinkedList<Integer> linkedList = new LinkedList<>();
        // 先插入
        linkedList.add(1);
        linkedList.add(0, 2);
        // 再排序
        Collections.sort(linkedList);
    }

    /**
     * 二分查找:  数据需是有序不重复的
     * 简介:	在二分搜寻法中，从数列的中间开始搜寻，如果这个数小于我们所搜寻的数，由于数列已排序，
     * 则该数左边的数一定都小于要搜寻的对象，
     * 所以无需浪费时间在左边的数；如果搜寻的数大于所搜寻的对象，则右边的数无需再搜寻，直接搜寻左边的数。
     *
     * @param nums 待查找数组
     * @num 待查找数
     */
    public static int search(int[] nums, int num) {
        int low = 0;
        int high = nums.length - 1;

        while (low <= high) {
            int mid = (low + high) / 2;

            //与中间值比较确定在左边还是右边区间,以调整区域
            if (num > nums[mid]) {
                low = mid + 1;
            } else if (num < nums[mid]) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return -1;
    }

    public boolean Find(int target, int[][] array) {
        for (int[] ints : array) {
            int low = 0;
            int high = ints.length - 1;
            while (low <= high) {
                int mid = (low + high) / 2;
                if (target > ints[mid]) {
                    low = mid + 1;
                } else if (target < ints[mid]) {
                    high = mid - 1;
                } else {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 快速排序
     * https://blog.csdn.net/shujuelin/article/details/82423852
     * quickSort(arr, 0, arr.length-1)
     */

    public static void quickSort(int[] arr, int low, int high) {
        int i, j, temp, t;
        if (low > high) {
            return;
        }
        i = low;
        j = high;
        //temp就是基准位
        temp = arr[low];

        while (i < j) {
            //先看右边，依次往左递减
            while (temp <= arr[j] && i < j) {
                j--;
            }
            //再看左边，依次往右递增
            while (temp >= arr[i] && i < j) {
                i++;
            }
            //如果满足条件则交换
            if (i < j) {
                t = arr[j];
                arr[j] = arr[i];
                arr[i] = t;
            }
        }
        //最后将基准数与i和j相等位置的数字交换
        arr[low] = arr[i];
        arr[i] = temp;
        //递归调用左半数组
        quickSort(arr, low, j - 1);
        //递归调用右半数组
        quickSort(arr, j + 1, high);
    }

    /**
     * 冒泡排序
     * 比较相邻的元素。如果第一个比第二个大，就交换他们两个。
     * 对每一对相邻元素作同样的工作，从开始第一对到结尾的最后一对。在这一点，最后的元素应该会是最大的数。
     *
     * @param numbers 需要排序的整型数组
     */
    public static void bubbleSort(int[] numbers) {
        int temp = 0;
        int size = numbers.length;
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - 1 - i; j++) {
                if (numbers[j] > numbers[j + 1])  //交换两数位置
                {
                    temp = numbers[j];
                    numbers[j] = numbers[j + 1];
                    numbers[j + 1] = temp;
                }
            }
        }
    }


    /**
     * 选择排序:原理即是，遍历元素找到一个最小（或最大）的元素，把它放在第一个位置，然后再在剩余元素中找到最小（或最大）的元素，
     * 把它放在第二个位置，依次下去，完成排序。
     *
     * @param arr
     */

    public static void selectSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {// 做第i趟排序
            int k = i;
            for (int j = k + 1; j < arr.length; j++) {// 选最小的记录
                if (arr[j] < arr[k]) {
                    k = j; //记下目前找到的最小值所在的位置
                }
            }
            //在内层循环结束，也就是找到本轮循环的最小的数以后，再进行交换
            if (i != k) {  //交换a[i]和a[k]
                int temp = arr[i];
                arr[i] = arr[k];
                arr[k] = temp;
            }
        }
    }

    //剑指 Offer 05
    //替换空格
    public String replaceSpace(String s) {
        StringBuilder str = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (String.valueOf(c).equals(" ")) {
                str.append("%20");
            } else {
                str.append(c);
            }
        }
        return str.toString();
    }

    //剑指 Offer 03
    //数组中重复的数字
    public int findRepeatNumber(int[] nums) {
        HashSet<Integer> list = new HashSet<>();
        for (int num : nums) {
            if (list.contains(num)) {
                return num;
            } else {
                list.add(num);
            }
        }
        return -1;
    }

    public class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }

    //剑指 Offer 06
    //从尾到头打印链表
    public int[] reversePrint(ListNode head) {
        ListNode node = head;
        int count = 0;
        while (node != null) {
            count++;
            node = node.next;
        }
        int[] nums = new int[count];
        node = head;
        for (int i = count - 1; i >= 0; i--) {
            nums[i] = node.val;
            node = node.next;
        }
        return nums;
    }

    //剑指 Offer 10- I. 斐波那契数列
    //写一个函数，输入 n ，求斐波那契（Fibonacci）数列的第 n 项。斐波那契数列的定义如下：
    //F(0) = 0,   F(1) = 1
    //F(N) = F(N - 1) + F(N - 2), 其中 N > 1.
    //斐波那契数列由 0 和 1 开始，之后的斐波那契数就是由之前的两数相加而得出。

    public int fib(int n) {
        if (n < 2) {
            return n;
        }
        return (fib(n - 1) + fib(n - 2)) % (int) (1e9 + 7);
    }

    public int fib2(int n) {
        if (n < 2) {
            return n;
        }
        //a、b为相邻两值
        int a = 0, b = 1, sum = 0;
        for (int i = 2; i <= n; i++) {
            sum = (a + b) % (int) (1e9 + 7);
            a = b;
            b = sum;
        }
        return sum;
    }

    //剑指 Offer 10- II. 青蛙跳台阶问题:同斐波那契数列 f(n) = f(n−1) + f(n−2)
    //https://leetcode-cn.com/problems/qing-wa-tiao-tai-jie-wen-ti-lcof/solution/mian-shi-ti-10-ii-qing-wa-tiao-tai-jie-wen-ti-dong/
    public int numWays(int n) {
        if (n < 2) {
            return 1;
        }
        int a = 1, b = 1, sum = 0;
        for (int i = 2; i <= n; i++) {
            sum = (a + b) % (int) (1e9 + 7);
            a = b;
            b = sum;
        }
        return sum;
    }

    // 剑指 Offer 11. 旋转数组的最小数字
    // [3, 4, 5,  6,  1, 2, 3]
    // [2, 3, 1,  1,  2, 2, 2]
    // [2, 3, 4,  1,  2, 2, 2]
    // https://leetcode-cn.com/problems/xuan-zhuan-shu-zu-de-zui-xiao-shu-zi-lcof/solution/mian-shi-ti-11-xuan-zhuan-shu-zu-de-zui-xiao-shu-3/
    public int minArray(int[] numbers) {
        int low = 0, high = numbers.length - 1;
        while (low < high) {
            int m = (low + high) / 2;
            if (numbers[m] > numbers[high]) low = m + 1;
            else if (numbers[m] < numbers[high]) high = m;
            else {
                int x = low;
                for (int k = low + 1; k < high; k++) {
                    if (numbers[k] < numbers[x]) x = k;
                }
                return numbers[x];
            }
        }
        return numbers[low];
    }

    /**
     * 1.位异或运算（^）
     * <p>
     * 运算规则是：两个数转为二进制，然后从高位开始比较，如果相同则为0，不相同则为1。
     * <p>
     * 比如：8^11.
     * <p>
     * 8转为二进制是1000，11转为二进制是1011.从高位开始比较得到的是：0011.然后二进制转为十进制，就是Integer.parseInt("0011",2)=3;
     * <p>
     * 2.位与运算符（&）
     * <p>
     * 运算规则：两个数都转为二进制，然后从高位开始比较，如果两个数都为1则为1，否则为0。
     * <p>
     * 比如：129&128.
     * <p>
     * 129转换成二进制就是10000001，128转换成二进制就是10000000。从高位开始比较得到，得到10000000，即128.
     * <p>
     * 3.位或运算符（|）
     * <p>
     * 运算规则：两个数都转为二进制，然后从高位开始比较，两个数只要有一个为1则为1，否则就为0。
     * <p>
     * 比如：129|128.
     * <p>
     * 129转换成二进制就是10000001，128转换成二进制就是10000000。从高位开始比较得到，得到10000001，即129.
     */

    // 剑指 Offer 15. 二进制中1的个数
    // https://leetcode-cn.com/problems/er-jin-zhi-zhong-1de-ge-shu-lcof/
    public int hammingWeight(int n) {
        return Integer.bitCount(n);
    }

    public int hammingWeight2(int n) {
        int res = 0;
        while (n != 0) {
            res += n & 1;
            n >>>= 1;
        }
        return res;
    }

    // 指 Offer 17. 打印从1到最大的n位数
    public int[] printNumbers(int n) {
        int size = (int) Math.pow(10, n) - 1;
        int[] nums = new int[size];
        for (int i = 0; i < size; i++) {
            nums[i] = i + 1;
        }
        return nums;
    }

    // 剑指 Offer 18. 删除链表的节点
    // https://leetcode-cn.com/problems/shan-chu-lian-biao-de-jie-dian-lcof/
    public ListNode deleteNode(ListNode head, int val) {
        if (head.val == val) return head.next;
        ListNode pre = head;
        ListNode cur = head.next;
        while (cur != null && cur.val != val) {
            pre = cur;
            cur = cur.next;
        }
        if (cur != null) pre.next = cur.next;
        return head;
    }

    // 剑指 Offer 21. 调整数组顺序使奇数位于偶数前面
    // https://leetcode-cn.com/problems/diao-zheng-shu-zu-shun-xu-shi-qi-shu-wei-yu-ou-shu-qian-mian-lcof/
    public int[] exchange(int[] nums) {
        int left = 0, right = nums.length - 1;
        while (left < right) {
            while (left < right && nums[left] % 2 != 0) {
                left++;
            }
            while (left < right && nums[right] % 2 == 0) {
                right--;
            }
            if (left < right) {
                int temp = nums[left];
                nums[left] = nums[right];
                nums[right] = temp;
            }
        }
        return nums;
    }

    // 剑指 Offer 22. 链表中倒数第k个节点
    // https://leetcode-cn.com/problems/lian-biao-zhong-dao-shu-di-kge-jie-dian-lcof/
    public ListNode getKthFromEnd(ListNode head, int k) {
        if (head == null) {
            return null;
        }
        ListNode node = head;
        int count = 0;
        while (node != null) {
            count++;
            node = node.next;
        }
        if (k > count) return null;
        for (int i = 1; i <= count - k; i++) {
            head = head.next;
        }
        return head;
    }

    public ListNode getKthFromEnd2(ListNode head, int k) {
        ListNode former = head, latter = head;
        for (int i = 0; i < k; i++) {
            if (former == null) return null;
            former = former.next;
        }
        while (former != null) {
            former = former.next;
            latter = latter.next;
        }
        return latter;
    }

    // 剑指 Offer 24. 反转链表
    // https://leetcode-cn.com/problems/fan-zhuan-lian-biao-lcof/
    public ListNode reverseList(ListNode head) {
        ListNode cur = head, pre = null;
        while (cur != null) {
            ListNode tmp = cur.next; // 暂存后继节点 cur.next
            cur.next = pre;          // 修改 next 引用指向
            pre = cur;               // pre 暂存 cur
            cur = tmp;               // cur 访问下一节点
        }
        return pre;
    }

    // 剑指 Offer 25. 合并两个排序的链表
    // https://leetcode-cn.com/problems/he-bing-liang-ge-pai-xu-de-lian-biao-lcof/
    // 输入：1->2->4, 1->3->4
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode dum = new ListNode(0), cur = dum;
        while (l1 != null && l2 != null) {
            if (l1.val < l2.val) {
                cur.next = l1;
                l1 = l1.next;
            } else {
                cur.next = l2;
                l2 = l2.next;
            }
            cur = cur.next;
        }
        cur.next = l1 != null ? l1 : l2;
        return dum.next;
    }

    // 剑指 Offer 40. 最小的k个数
    // https://leetcode-cn.com/problems/zui-xiao-de-kge-shu-lcof/
    public int[] getLeastNumbers(int[] arr, int k) {
        Arrays.sort(arr);
        int[] nums = new int[k];
        for (int i = 0; i < nums.length; i++) {
            nums[i] = arr[i];
        }
        return nums;
    }

    // 剑指 Offer 39. 数组中出现次数超过一半的数字
    // https://leetcode-cn.com/problems/shu-zu-zhong-chu-xian-ci-shu-chao-guo-yi-ban-de-shu-zi-lcof/solution/mian-shi-ti-39-shu-zu-zhong-chu-xian-ci-shu-chao-3/
    public int majorityElement(int[] nums) {
        int x = 0, votes = 0;
        for (int num : nums) {
            if (votes == 0) x = num;
            votes += num == x ? 1 : -1;
        }
        return x;
    }


}
