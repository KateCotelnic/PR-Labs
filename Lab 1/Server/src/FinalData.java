public class FinalData {
    private String[] header = new String[200000000];
    private String[][] table = new String[20000][2000];

    public String[] getHeader() {
        return header;
    }

    public void addHeader(String header, int k) {
        this.header[k] = header;
        k++;
    }
    public int getHeaderSize(){
        int i = 0;
        while (this.header[i]!=null)
            i++;
        return i;
    }

    public String[][] getTable() {
        return table;
    }

    public void addToTable(String str, int i, int j) {
        this.table[i][j] = str;
    }

    public void print(){
        int k = 0;
        while (this.header[k] != null) {
            System.out.print(this.header[k] + " | ");
            k++;
        }
        System.out.println();
        for (int i = 0; i < 70; i++){
            for (int j=0;j<this.table[i].length;j++){
                System.out.print(this.table[i][j] + " | ");
            }
            System.out.println();
        }
    }
}
