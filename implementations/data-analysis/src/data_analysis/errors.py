class DataDirNotSetError(RuntimeError):
    def __init__(self, env_var: str):
        super().__init__(f"{env_var} is not set. Point it at the trade-plumber data folder.")
