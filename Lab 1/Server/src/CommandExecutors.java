class CommandExecutors{
    private String reader;

    public CommandExecutors(String reader) {
        this.reader = reader;
    }

    public String getReader() {
        return reader;
    }

    public void setReader(String reader) {
        this.reader = reader;
    }

    public String selectColumns() {
        String header = "";
        for (int k =0;k<Server.data.getHeader().length;k++){
            if(Server.data.getHeader()[k]!=null) {
                header += Server.data.getHeader()[k] + "   ";
            }
        }
        return header;
    }

    public String selectRow(){
        int i=0;
        while (reader.charAt(i)!=' '){
            i++;
        }
        String s = reader.substring(i+1);
        i = Integer.parseInt(s);
        s = "";
        for(int j = 0; j < Server.data.getTable()[0].length; j++){
            if(Server.data.getTable()[i][j] != null){
                s += Server.data.getTable()[i][j] + "  ";
            }
        }
        if(s.equals(""))
            return "No such row";
        return s;
    }

    public String selectColumn() {
        int i=0;
        while (reader.charAt(i)!=' '){
            i++;
        }
        String s = reader.substring(i+1);
        int j=0;
//        System.out.println(s);
        while (j<Server.data.getHeaderSize() && !Server.data.getHeader()[j].equals(s)) {
            j++;
        }
        if(j == Server.data.getHeaderSize())
            return "There is no such column";
        s = "";
        for(i =0; i<Server.data.getTable().length;i++){
            if(Server.data.getTable()[i][j]!=null){
                s+=Server.data.getTable()[i][j] + "  ";
            }
        }
        return s;
    }

    public String selectFromColumn(){
        int i=0; int k=0;
        while (reader.charAt(i)!=' '){
            i++;
        }
        k = i+1;
        i++;
        while (reader.charAt(i) != ' ') {
            i++;
        }
        String s = reader.substring(k,i);
        String name = reader.substring(i+1);
        int j=0;
        while (j<Server.data.getHeaderSize() && !Server.data.getHeader()[j].equals(s)) {
            j++;
        }
        if(j == Server.data.getHeaderSize())
            return "There is no such column";
        s = "";
        for(i =0; i<Server.data.getTable().length;i++){
            if(Server.data.getTable()[i][j]!=null && Server.data.getTable()[i][j].contains(name)){
                s+=Server.data.getTable()[i][j] + "  ";
            }
        }
        if(s.equals(""))
            return "No such word in the column";
        return s;
    }
    
    public String selectRowWith(){
        int i=0;
        while (reader.charAt(i)!=' '){
            i++;
        }
        String name = reader.substring(i+1);
//        System.out.println(name);
        String s = "";
        for(i =0; i<Server.data.getTable().length;i++){
            for(int j=0; j<Server.data.getTable()[i].length;j++){
                if(Server.data.getTable()[i][j]!=null && Server.data.getTable()[i][j].contains(name)) {
                    for(int k=0;k<Server.data.getTable()[i].length;k++){
                        if(Server.data.getTable()[i][j]!=null){
                            s += Server.data.getTable()[i][k] + "  ";
                        }
                    }
                    return s;
                }
            }
        }
        return "No such word in the data";
    }

}
