package cz.uhk.springjourneyplanner.engine;

import cz.uhk.springjourneyplanner.dto.Path;

import java.util.*;

public class PathFilter {

    public static List<Path> filterPaths(List<Path> paths){
        List<Path> newList = new ArrayList<>();
        List<List<String>> uniqueLines = new ArrayList<>();
        for (Path path : paths){
            if (uniqueLines.stream().noneMatch(lines -> isSubList(path.getLines(), lines))){
                uniqueLines.add(path.getLines());
                newList.add(path);
            }
        }
        int minTransfers = newList.stream().map(path -> path.getConnections().size()).min(Comparator.comparingInt(size ->size)).get();
        return newList.stream().filter(path -> path.getConnections().size() == minTransfers).toList();
    }


    /**
     * zjisti, jestli je supposedSubList serazena podmnozina hlavniho listu.
     * V hlavnim listu se musi nachazet vsechny prvky sublistu ve spravnem poradi
     * @param list
     * @param supposedSubList
     * @return zda je supposedSubList serazenou podmnozinou listu
     */
    private static boolean isSubList(List<String> list, List<String> supposedSubList){
        if (supposedSubList.size() > list.size()){
            return false;
        }
        int supposedIndex = 0;
        for (int i = 0; i < list.size(); i++){
            String supposed = supposedSubList.get(supposedIndex);
            String main = list.get(i);
            if (supposed.equals(main)){
                supposedIndex++;
                if (supposedIndex == supposedSubList.size()){
                    return true;
                }
            }
        }
        return supposedIndex == supposedSubList.size();
    }

}
