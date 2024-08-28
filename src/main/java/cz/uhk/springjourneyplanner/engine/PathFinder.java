package cz.uhk.springjourneyplanner.engine;

import cz.uhk.springjourneyplanner.dto.*;

import java.util.*;

public class PathFinder {

    public static List<List<NodeLine>> findPath(Node starting, String goal){
        Map<Node, List<NodeLine>> shortestFrom = new HashMap<>(100);
        Map<Integer, Set<Node>> layers = new HashMap<>();
        int time = starting.getTime();
        int shortestToGoal = 1440 * 2;
        layers.put(starting.getTime(), setWith(starting));

        NodeLine finished = layeredSearch(goal, shortestFrom, layers, time, shortestToGoal);

        if (finished == null){
            return null;
        }

        List<List<NodeLine>> paths = new ArrayList<>();
        List<NodeLine> currentPath = new ArrayList<>();
        currentPath.add(finished);
        dfs(shortestFrom, finished, starting, currentPath, paths);
        paths = reverseAndOrderPaths(paths);
        return paths;
    }

    /**
     * napsat, jak algoritmus vyhledava...
     */
    private static NodeLine layeredSearch(String goal, Map<Node, List<NodeLine>> shortestFrom, Map<Integer,
            Set<Node>> layers, int time, int shortestToGoal) {
        NodeLine finished = null;
        while (time < shortestToGoal){
            for (Node node : layers.get(time)){
                for (NodeLine nd : node.getNeighbors()){
                    Node neighbor = nd.node();
                    addToLayerMap(layers, neighbor);
                    NodeLine newND = new NodeLine(node, nd.line());
                    addToDistanceMap(shortestFrom, newND, neighbor);
                    if (neighbor.getStop().equals(goal)){
                        if (neighbor.getTime() < shortestToGoal){
                            shortestToGoal = neighbor.getTime();
                            finished = new NodeLine(neighbor, null);
                        }
                    }
                }
            }
            time++;
        }
        return finished;
    }

    private static List<List<NodeLine>> reverseAndOrderPaths(List<List<NodeLine>> paths){
        paths.forEach(Collections::reverse);
        return paths.stream()
                .map(PathFinder::filterPath)
                .sorted(Comparator.comparingInt(path -> path.get(0).node().getTime()))
                .toList();
    }


    /**
     * pouziva se po nalezeni nejkratsi cesty pro zpetne sestavovani cele path (nebo cest, muze byt vice, co trvaji stejne)
     */
    private static void dfs(Map<Node, List<NodeLine>> graph, NodeLine current, Node sink, List<NodeLine> currentPath, List<List<NodeLine>> allPaths) {
        if (current.node() == sink) {
            allPaths.add(new ArrayList<>(currentPath));
            return;
        }
        for (NodeLine neighbor : graph.get(current.node())) {
            currentPath.add(neighbor);
            dfs(graph, neighbor, sink, currentPath, allPaths);
            currentPath.remove(currentPath.size() - 1);
        }
    }

    /**
     * profiltruje kazdou path tak, aby tam mezi spojenimi byla vzdy jen jedna "cekaci" part
     * @param path original path
     * @return new path
     */
    private static List<NodeLine> filterPath(List<NodeLine> path){
        List<NodeLine> newPath = new ArrayList<>();
        boolean waitIncluded = false;
        boolean initialWaitSurpased = false;
        for (int i = 0; i < path.size(); i++){
            NodeLine pathPart = path.get(i);
            boolean isWaitingPart = pathPart.line() == null;
            if (isWaitingPart && !initialWaitSurpased) {
                continue;
            } else {
                initialWaitSurpased = true;
            }
            if (!(waitIncluded && isWaitingPart)){
                newPath.add(pathPart);
                waitIncluded = isWaitingPart;
            }
        }
        return newPath;
    }

    private static void addToDistanceMap(Map<Node, List<NodeLine>> shortestFrom, NodeLine nd, Node neighbor) {
        if (shortestFrom.containsKey(neighbor)){
            shortestFrom.get(neighbor).add(nd);
        } else {
            List<NodeLine> listNL = new ArrayList<>();
            listNL.add(nd);
            shortestFrom.put(neighbor, listNL);
        }
    }

    private static void addToLayerMap(Map<Integer, Set<Node>> layers, Node node){
        if (layers.containsKey(node.getTime())){
            layers.get(node.getTime()).add(node);
        } else {
            layers.put(node.getTime(), setWith(node));
        }
    }

    private static Set<Node> setWith(Node node){
        Set<Node> set = new HashSet<>();
        set.add(node);
        return set;
    }

}
