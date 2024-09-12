package cz.uhk.springjourneyplanner.engine;

import cz.uhk.springjourneyplanner.dto.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class PathFinder {

    public static List<Path> findPath(Node starting, String goal){
        Map<Node, List<NodeLine>> shortestFrom = new HashMap<>(100);
        Map<Integer, List<Node>> layers = new HashMap<>();
        int time = starting.getTime();
        int shortestToGoal = 1440 * 2;
        layers.put(starting.getTime(), listWith(starting));

        NodeLine finished = layeredSearch(goal, shortestFrom, layers, time, shortestToGoal);

        if (finished == null){
            return null;
        }

        List<List<NodeLine>> paths = new ArrayList<>();
        List<NodeLine> currentPath = new ArrayList<>();
        currentPath.add(finished);

        AtomicInteger maxTransferCount = new AtomicInteger(Integer.MAX_VALUE);
        AtomicInteger currentTransferCount = new AtomicInteger(0);
        dfs(shortestFrom, finished, starting, currentPath, paths, maxTransferCount, currentTransferCount);
        return reverseAndOrderPaths(paths);
    }

    /**
     * napsat, jak algoritmus vyhledava...
     */
    private static NodeLine layeredSearch(String goal, Map<Node, List<NodeLine>> shortestFrom,
                                          Map<Integer, List<Node>> layers, int time, int shortestToGoal) {
        NodeLine finished = null;
        while (time < shortestToGoal){
            layers.remove(time - 1);
            List<Node> nodeSet = layers.get(time);
            if (nodeSet == null){
                continue;
            }
            for (int i = 0; i < nodeSet.size(); i++){
                Node node = nodeSet.get(i);
                for (NodeLine nd : node.getNeighbors()) {
                    Node neighbor = nd.node();
                    addToLayerMap(layers, neighbor);
                    NodeLine newND = new NodeLine(node, nd.line());
                    addToDistanceMap(shortestFrom, newND, neighbor);
                    if (neighbor.getStop().equals(goal)) {
                        if (neighbor.getTime() < shortestToGoal) {
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

    private static List<Path> reverseAndOrderPaths(List<List<NodeLine>> paths){
        paths.forEach(Collections::reverse);
        return paths.stream()
                .map(PathFinder::filterPath)
                .sorted(Comparator.comparingInt(path -> path.getConnections().get(0).startTime()))
                .toList();
    }


    /**
     * pouziva se po nalezeni nejkratsi cesty pro zpetne sestavovani cele path (nebo cest, muze byt vice, co trvaji stejne)
     */
    private static void dfs(Map<Node, List<NodeLine>> graph, NodeLine current, Node sink,
                            List<NodeLine> currentPath, List<List<NodeLine>> allPaths,
                            AtomicInteger maxTransferCount, AtomicInteger currentTransferCount) {

        if (current.node() == sink) {
            maxTransferCount.set(currentTransferCount.get());
            allPaths.add(new ArrayList<>(currentPath));
            return;
        }
        for (NodeLine neighbor : graph.get(current.node())) {
            boolean neighborIsLine = neighbor.line() != null;
            if (neighborIsLine && currentTransferCount.get() == maxTransferCount.get()) continue;
            currentPath.add(neighbor);
            if (neighborIsLine) currentTransferCount.incrementAndGet();
            dfs(graph, neighbor, sink, currentPath, allPaths, maxTransferCount, currentTransferCount);
            NodeLine removed = currentPath.remove(currentPath.size() - 1);
            if (removed.line() != null) currentTransferCount.decrementAndGet();

        }
    }

    /**
     * profiltruje kazdou path tak, aby tam mezi spojenimi byla vzdy jen jedna "cekaci" part
     * @param path original path
     * @return new path
     */
    private static Path filterPath(List<NodeLine> path){
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
        return Path.toPath(newPath);
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

    private static void addToLayerMap(Map<Integer, List<Node>> layers, Node node){
        if (layers.containsKey(node.getTime())){
            List<Node> discoveredNodes = layers.get(node.getTime());
            if (!discoveredNodes.contains(node)){
                layers.get(node.getTime()).add(node);
            }
        } else {
            layers.put(node.getTime(), listWith(node));
        }
    }

    private static List<Node> listWith(Node node){
        List<Node> list = new ArrayList<>();
        list.add(node);
        return list;
    }

}
