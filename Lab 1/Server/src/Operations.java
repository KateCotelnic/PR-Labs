class SelectAllColumnNamesOperation implements Command{
    private CommandExecutors commandExecutors;

    public SelectAllColumnNamesOperation(CommandExecutors commandExecutors) {
        this.commandExecutors = commandExecutors;
    }

    @Override
    public String execute() {
        return commandExecutors.selectColumns();
    }
}

class SelectColumnOperation implements Command{
    private CommandExecutors commandExecutors;

    public SelectColumnOperation(CommandExecutors commandExecutors) {
        this.commandExecutors = commandExecutors;
    }

    @Override
    public String execute() {
        return commandExecutors.selectColumn();
    }
}

class SelectFromColumnOperation implements Command{
    private CommandExecutors commandExecutors;

    public SelectFromColumnOperation(CommandExecutors commandExecutors) {
        this.commandExecutors = commandExecutors;
    }

    @Override
    public String execute() {
        return commandExecutors.selectFromColumn();
    }
}

class SelectRowWithOperation implements Command{
    private CommandExecutors commandExecutors;

    public SelectRowWithOperation(CommandExecutors commandExecutors) {
        this.commandExecutors = commandExecutors;
    }

    @Override
    public String execute() {
        return commandExecutors.selectRowWith();
    }
}

class SelectRowOperation implements Command{
    private CommandExecutors commandExecutors;

    public SelectRowOperation(CommandExecutors commandExecutors) {
        this.commandExecutors = commandExecutors;
    }

    @Override
    public String execute() {
        return commandExecutors.selectRow();
    }
}
