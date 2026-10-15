from pathlib import Path


class DataDirNotSetError(RuntimeError):
    def __init__(self, env_var: str):
        super().__init__(f"{env_var} is not set. Point it at the trade-plumber data folder.")


class BarsFileNotFoundError(FileNotFoundError):
    def __init__(self, path: Path):
        super().__init__(f"No bars file at {path}. Run a trade-plumber update for this symbol first.")
