public class Algo_factory {
    public Algorithm createAlgo(String algoName) {
        if (algoName == null)
            return null;
        switch (algoName) {
            case "A*": {
                return new AStarAlgo();
            }
            case "DFID": {
                return new DFIDAlgo();
            }
            case "IDA*" : {
                return new IDAStarAlgo();
            }
            case "DFBnB" : {
                return new DfBnBAlgo();
            }
            default :
                throw new IllegalArgumentException("Unknown Algorithm name " + algoName);
        }
    }
}
