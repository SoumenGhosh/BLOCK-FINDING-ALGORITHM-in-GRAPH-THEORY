
/* Author: SOUMEN GHOSH
 * FILE CREATED: 10th October
 */


import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.Collections;

public class BlockInGraph {
	
	static int vertex, edge;
	static ArrayList<Integer> vertices[];
	static int dfs_low[];
	static int dfs_num[];
	static int counter;
	static boolean visited[];
	static ArrayList<Integer> cut;
	static Stack<Integer> stack;
	static ArrayList<ArrayList<Integer>> block;
	
	public static void main(String[] args) {	
		try {
			File f = new File("/Users/user/eclipse-workspace/BlockFinding/src/myInput.txt");
			Scanner sc = new Scanner(f);
			vertex = sc.nextInt();
			edge = sc.nextInt();
			
			vertices = new ArrayList[vertex];
			for(int i = 0; i < vertex; ++i) {
				vertices[i] = new ArrayList<Integer>();
			}

			for(int i = 0; i < edge; i++) {
				int a = sc.nextInt();
				int b = sc.nextInt();
				
				vertices[a].add(b);
				vertices[b].add(a);
			}
			
			initial();
			start();
		} catch(Exception ex) {
			System.out.println(ex);
		}	
	}
	
	static void initial() {
		counter = 0;
		dfs_low = new int[vertex]; 
		dfs_num = new int[vertex];
		visited = new boolean[vertex]; 
		cut = new ArrayList<Integer>();
		stack = new Stack<Integer>();
		block = new ArrayList<ArrayList<Integer>>();
	}
	
	static void start() {
		for(int i = 0; i < vertex; i++) {
			if(dfs_num[i] == 0) dfs(i, -1);
		}
		
		int count = 0;
		for(int i = 0;i<cut.size();i++) {
			if(cut.get(i) == 0) count++;
		}
		
		if(count == 1) {
			int index= -1;
			for(int i=0;i<cut.size();i++) {
				if(cut.get(i) == 0) index = i;
			}
			cut.remove(index);
		}
		
		cut = (ArrayList<Integer>) cut.stream().distinct().collect(Collectors.toList());
		System.out.println("cut vertices: "+cut);
		for (int i = 0; i < block.size(); i++) {
			System.out.println("block " + (i + 1) +": "+ block.get(i));
		}
	}
	
	static void dfs(int curr, int parent){
		visited[curr] = true;
		counter++;
		dfs_num[curr] = counter;
		dfs_low[curr] = counter;
		stack.push(curr);
		
		for (int i = 0; i < vertices[curr].size(); i++) {
			int nextVertex = vertices[curr].get(i);
			
			if(visited[nextVertex] == false) { 
				dfs(nextVertex, curr); 
				dfs_low[curr] = Math.min(dfs_low[nextVertex], dfs_low[curr]);
				if(parent == -1 && vertices[curr].size() > 1) {
					makeBlock(nextVertex, curr);
				}
				
				if(parent != -1 && dfs_low[nextVertex] >= dfs_num[curr]){ 
					makeBlock(nextVertex, curr);
				} 
			}
			else dfs_low[curr] = Math.min(dfs_low[curr], dfs_num[nextVertex]);
		}
	}
	
	static void makeBlock(int nextVertex, int curr) {
		ArrayList<Integer> component = new ArrayList<Integer>();
		int temp;
		while(true){
			temp = stack.pop();
			component.add(temp);
			if(temp == nextVertex) break;
		}
		cut.add(curr);
		component.add(curr);
		Collections.reverse(component);
		block.add(component);
	}
}




