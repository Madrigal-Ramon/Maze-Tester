import java.io.*;
import java.util.*;

public class MazeSolver {
    private char[][] maze;
    private boolean solvable;
    private static final int SIZE = 20;

    public MazeSolver(char[][] maze) {
        this.maze = maze;
        this.solvable = determineSolvability();
    }

    private boolean determineSolvability() {
        int startRow = -1, startCol = -1, endRow = -1, endCol = -1;
        
        // Find start and end positions
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (maze[i][j] == 'S') {
                    startRow = i;
                    startCol = j;
                } else if (maze[i][j] == 'E') {
                    endRow = i;
                    endCol = j;
                }
            }
        }
        
        if (startRow == -1 || endRow == -1) {
            return false; // No start or end found
        }

        // BFS implementation (same as your canSolve method)
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        boolean[][] visited = new boolean[SIZE][SIZE];
        Queue<int[]> queue = new LinkedList<>();
        
        queue.offer(new int[]{startRow, startCol});
        visited[startRow][startCol] = true;
        
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int row = current[0];
            int col = current[1];
            
            if (row == endRow && col == endCol) {
                return true;
            }
            
            for (int[] dir : directions) {
                int newRow = row + dir[0];
                int newCol = col + dir[1];
                
                if (newRow >= 0 && newRow < SIZE && newCol >= 0 && newCol < SIZE && 
                    !visited[newRow][newCol] && 
                    maze[newRow][newCol] != '#') {
                    
                    visited[newRow][newCol] = true;
                    queue.offer(new int[]{newRow, newCol});
                }
            }
        }
        
        return false;
    }

    @Override
    public String toString() {
        return "Maze: " + (solvable ? "YES" : "NO");
    }

    public static List<MazeSolver> readMazes(String filename) throws IOException {
        List<MazeSolver> mazes = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        
        List<String> mazeLines = new ArrayList<>();
        String line;
        
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            
            if (line.isEmpty()) {
                if (mazeLines.size() == SIZE) {
                    char[][] mazeArray = createMazeArray(mazeLines);
                    mazes.add(new MazeSolver(mazeArray));
                }
                mazeLines.clear();
            } else {
                mazeLines.add(line);
                if (mazeLines.size() == SIZE) {
                    char[][] mazeArray = createMazeArray(mazeLines);
                    mazes.add(new MazeSolver(mazeArray));
                    mazeLines.clear();
                }
            }
        }
        
        if (mazeLines.size() == SIZE) {
            char[][] mazeArray = createMazeArray(mazeLines);
            mazes.add(new MazeSolver(mazeArray));
        }
        
        reader.close();
        return mazes;
    }

    private static char[][] createMazeArray(List<String> lines) {
        char[][] maze = new char[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            String line = lines.get(i);
            for (int j = 0; j < SIZE; j++) {
                if (j < line.length()) {
                    maze[i][j] = line.charAt(j);
                } else {
                    maze[i][j] = ' ';
                }
            }
        }
        return maze;
    }

    public static void main(String[] args) {
        try {
            List<MazeSolver> mazes = readMazes("maze.dat");

            for (int i = 0; i < mazes.size(); i++) {
                MazeSolver maze = mazes.get(i);
                System.out.println(maze.toString());
            }
            
        } catch (IOException e) {
            System.out.println("Error: Could not read maze.dat file");
        }
    }
}