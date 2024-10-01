import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.regex.*;
import java.util.stream.*;

import javax.management.RuntimeErrorException;


import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;



class Result {

    /*
     * Complete the 'mostBalancedPartition' function below.
     *
     * The function is expected to return an INTEGER.
     * The function accepts following parameters:
     *  1. INTEGER_ARRAY parent
     *  2. INTEGER_ARRAY files_size
     */
    public static int mostBalancedPartition(List<Integer> parent, List<Integer> files_size) {
        int n = parent.size();

        // Step 1: Create a mapping from parent to children
        Map<Integer, List<Integer>> parentToChildren = new HashMap<>();
        for (int i = 0; i < n; i++) {
            parentToChildren.computeIfAbsent(parent.get(i), k -> new ArrayList<>()).add(i);
        }

        // Step 2: Calculate total sizes for each subtree
        Map<Integer, Integer> totalSizes = new HashMap<>();
        int totalSum = computeTotalSizes(-1, parentToChildren, files_size, totalSizes);

        // Step 3: Find the minimum difference
        int minDiff = Integer.MAX_VALUE;

        for (Map.Entry<Integer, Integer> entry : totalSizes.entrySet()) {
            int childIndex = entry.getKey();
            if (childIndex != -1) { // Skip the root
                int leftSize = entry.getValue();
                int rightSize = totalSum - leftSize;
                minDiff = Math.min(minDiff, Math.abs(leftSize - rightSize));
            }
        }

        return minDiff;
    }

    private static int computeTotalSizes(int parent, Map<Integer, List<Integer>> parentToChildren, List<Integer> files_size, Map<Integer, Integer> totalSizes) {
        int totalSize = 0;

        // If this node is not the root, add its file size
        if (parent != -1) {
            totalSize += files_size.get(parent); // Add the file size of the current parent
        }

        // If this parent has children, calculate their sizes recursively
        if (parentToChildren.containsKey(parent)) {
            for (int childIndex : parentToChildren.get(parent)) {
                totalSize += computeTotalSizes(childIndex, parentToChildren, files_size, totalSizes);
            }
        }

        // Store the total size for this parent
        totalSizes.put(parent, totalSize);
        return totalSize;
    }
}



public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int parentCount = Integer.parseInt(bufferedReader.readLine().trim());

        List<Integer> parent = IntStream.range(0, parentCount).mapToObj(i -> {
            try {
                return bufferedReader.readLine().replaceAll("\\s+$", "");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        })
            .map(String::trim)
            .map(Integer::parseInt)
            .collect(toList());

        int files_sizeCount = Integer.parseInt(bufferedReader.readLine().trim());

        List<Integer> files_size = IntStream.range(0, files_sizeCount).mapToObj(i -> {
            try {
                return bufferedReader.readLine().replaceAll("\\s+$", "");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        })
            .map(String::trim)
            .map(Integer::parseInt)
            .collect(toList());

        int result = Result.mostBalancedPartition(parent, files_size);

        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();

        bufferedReader.close();
        bufferedWriter.close();
    }
}
