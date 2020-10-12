public class FinalData {
    private String[] header = new String[16];
    private String[][] table = new String[70][16];

    public String[] getHeader() {
        return header;
    }

    public void addHeader(String header, int k) {
        this.header[k] = header;
        k++;
    }

    public String[][] getTable() {
        return table;
    }

    public void addToTable(String str,int i,int j) {
        this.table[i][j] = str;
    }

    public void print(){
        for (int k=0;k<this.header.length;k++) {
            System.out.print(this.header[k] + " | ");
        }
        System.out.println();
        for (int i=0;i<this.table.length;i++){
            for (int j=0;j<this.table[i].length;j++){
                System.out.print(this.table[i][j] + " | ");
            }
            System.out.println();
        }
    }
}
