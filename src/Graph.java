import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;


public class Graph {
	/*num_V:number of vertices
	 *num_E:number of edges =num_V-1 because of the property of tree
	 *num_leaf:number of leaves
	 *label_size:the size of the labelling alphabet
	 *vertices:Adjacency List of the graph.The first node of the i th Linkedlist is the i th vertices 
	 */
	int num_V;
	int num_E;
	int num_leaf;
	int label_size;
	NodeList[] vertices;
	
	Graph(String fileName,int label_size) throws IOException {
		/*
		 * Load the document to form the graph in adjlist
		 */
		
		this.label_size=label_size;
		
		List<String> lines = Files.readAllLines(Paths.get(fileName));
		//System.out.println(lines.get(0));
		String[] firstline=lines.get(0).split(" ");
		//System.out.println(firstline.length);
		//System.out.println(firstline[0]);
		//System.out.println(firstline[1]);
		this.num_V=Integer.valueOf(firstline[0]);
		this.num_E=this.num_V-1;
		this.num_leaf=Integer.valueOf(firstline[1]);
		
		this.vertices=new NodeList[this.num_V];
		for (int i=0;i<this.num_V;i++) {
			this.vertices[i]=new NodeList(label_size);
			this.vertices[i].list.addFirst(new Node(i,label_size));
		}
		for(int i=1;i<this.num_V;i++) {
			String[] line =lines.get(i).split(" ");
			//System.out.println(lines.get(i));
			int st=Integer.valueOf(line[0]);
			int ed=Integer.valueOf(line[1]);
			this.vertices[st-1].list.addLast(this.vertices[ed-1].list.get(0));
			this.vertices[ed-1].list.addLast(this.vertices[st-1].list.get(0));
		}		
		for(int i=0;i<this.num_leaf;i++) {
			String[] line =lines.get(this.num_V+i).split(" ");
			int leaf_idx=Integer.valueOf(line[0]);
			Node leaf=this.vertices[leaf_idx-1].list.get(0);
			for (int j=0;j<line[1].length();j++) {
				if (line[1].charAt(j)=='$'){
					break;
				}
				//System.out.println(line[1].charAt(j));
				leaf.labels[line[1].charAt(j)-65]=1;
			}
			for (int k=0;k<this.label_size;k++) {
				leaf.is_visited[k]=true;
				leaf.is_labeled[k]=true;
				if (leaf.labels[k]==1){
					leaf.weight[2*k]=1;
					leaf.weight[2*k+1]=0;
				}
				else {
					leaf.weight[2*k]=0;
					leaf.weight[2*k+1]=1;
				}
			}
			
			//this.vertices[leaf_idx].list.get(0);
		}
	}
	void update_weight(int idx,int label_pos) {
		/*
		 * The process of dynamic programming. Utilizes DFS for the tree and updata weight w[0]
		 * and w[1] for each node from bottom to root
		 */
		int w_0=0;
		int w_1=0;
		LinkedList<Node> curr_list=this.vertices[idx].list;
		curr_list.get(0).is_visited[label_pos]=true;
		for (Node n:curr_list) {
			if(!n.is_visited[label_pos]) {
				this.update_weight(n.idx,label_pos);
			}
			w_0+=Math.min(n.weight[2*label_pos], 1+n.weight[2*label_pos+1]);
			w_1+=Math.min(n.weight[2*label_pos+1], 1+n.weight[2*label_pos]);
		}
		Node curr_node=this.vertices[idx].list.get(0);
		curr_node.weight[2*label_pos]=w_0;
		curr_node.weight[2*label_pos+1]=w_1;
	}
	
	void label(int idx,int label_pos) {
		/*
		 * Once the label of the root is determined, revisiting each vertice from top to bottom
		 * to label each vertices 
		 */
		LinkedList<Node> curr_list=this.vertices[idx].list;
		int l=curr_list.get(0).labels[label_pos];
		
		for (Node n:curr_list) {
			if(!n.is_labeled[label_pos]) {	
				n.is_labeled[label_pos]=true;
				n.labels[label_pos]=n.weight[2*label_pos+l]<(1+n.weight[2*label_pos+1-l]) ?l:1-l;
				this.label(n.idx,label_pos);
			    }
			}
	}
	
	int solve_single_bilabel(int label_pos) {
		/*
		 * The solver for a tree containing the label with only one character
		 */
		int root_indice=0;
		Node root=this.vertices[0].list.get(0);
		for(int i=0;i<this.num_V;i++) {
			if(!this.vertices[i].list.get(0).is_labeled[label_pos]) {
				root_indice=i;
				root=this.vertices[i].list.get(0);
				break;
			}
		}
		update_weight(root_indice,label_pos);
		root.is_labeled[label_pos]=true;
		if (root.weight[2*label_pos]<root.weight[2*label_pos+1]) {
			root.labels[label_pos]=0;
			label(root_indice,label_pos);
			return root.weight[2*label_pos];
		}
		else {
			root.labels[label_pos]=1;
			label(root_indice,label_pos);
			return root.weight[2*label_pos+1];
		}
		//return Math.min(root.weight[2*label_pos],root.weight[2*label_pos+1]);
	}
	
	void solve() {
		/*
		 * The overall solver for each labelling tree
		 */
		int sum=0;
		for(int i=0;i<this.label_size;i++) {
			sum+=solve_single_bilabel(i);
		}
		System.out.println(sum);
	}
	
	void print_G() {
		/*
		 * Output format of G
		 */
		System.out.printf("AdjList:\n");
		for(int i=0;i<this.num_V;i++) {
			//System.out.print(this.vertices[i].list.get(0).is_visited);
			System.out.print(Utils.Onehot2String(this.vertices[i].list.get(0).labels));
			for(Node n:this.vertices[i].list) {
				System.out.print(n.idx+1);
				
				System.out.print(" -> ");
			}
			System.out.println("");
			//System.out.println(this.vertices[i].list.toString());
		}
	}
	

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		int label_size=26;
		String filename="C:\\Users\\lc010\\eclipse-workspace\\INF421\\input\\0.in";
		Graph G=new Graph(filename,label_size);
		//G.print_G();
		G.solve();
		//G.print_G();

	}

}


class Node {
	int idx;
	int[] labels;
	boolean[] is_visited; // For DFS in updating weight
	boolean[] is_labeled; // For DFS in labelling process
	int[] weight; // size:label_size*2 weight[2*label_pos]=w0(label_pos) weight[2*label_pos+1]=w1(label_pos)
	
	Node(int idx,int label_size){
		this.idx=idx;
		this.labels=new int[label_size];
		this.weight=new int[label_size*2];
		this.is_visited=new boolean[label_size];
		this.is_labeled=new boolean[label_size];
	}
}

class NodeList{
	public LinkedList<Node> list;
	NodeList(int label_size){
		this.list=new LinkedList<Node>();
		//this.list.addFirst(new Node(label_size));
	}
}
