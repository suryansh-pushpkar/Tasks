import time
from rich.console import Console
from rich.panel import Panel
from rich.text import Text
from rich.prompt import Prompt, Confirm, IntPrompt
from rich.progress import Progress, SpinnerColumn, TextColumn
from rich.table import Table

# Initialize the rich console
console = Console()

def show_welcome():
    """Displays a colourful welcome panel."""
    welcome_text = Text("\n🚀 init-app — Enterprise Java Scaffolding Engine\n", style="bold cyan")
    welcome_text.append("Final Master Documentation (Java/Jakarta EE Edition)", style="italic green")
    
    # Text inside the panel
    panel_content = Text.assemble(
        ("Welcome to the Interactive Java Project Wizard!\n\n", "bold bright_white"),
        ("This tool will guide you through creating a production-ready\n", "white"),
        ("Spring Boot or Jakarta EE application.\n\n", "white"),
        ("Press ", "white"),
        ("Ctrl+C", "bold red"),
        (" at any time to exit.", "white")
    )

    console.print(Panel(panel_content, title="[bold cyan]Initialization[/bold cyan]", border_style="cyan"))

def get_project_metadata():
    """Step 1: GAV Metadata."""
    console.print("\n[bold yellow]Step 1: Project Identity (GAV)[/bold yellow]")
    group_id = Prompt.ask("Enter [bold green]Group ID[/bold green] (e.g., com.webkorps)", default="com.example")
    artifact_id = Prompt.ask("Enter [bold green]Artifact ID[/bold green] (e.g., user-service)")
    version = Prompt.ask("Project [bold green]Version[/bold green]", default="0.0.1-SNAPSHOT")
    return group_id, artifact_id, version

def get_core_stack():
    """Step 2: Core Stack Selection."""
    console.print("\n[bold yellow]Step 2: Core Stack[/bold yellow]")
    
    build_tool = Prompt.ask(
        "Select [bold green]Build Tool[/bold green]",
        choices=["Maven", "Gradle (Groovy)", "Gradle (Kotlin)"],
        default="Maven"
    )
    
    framework = Prompt.ask(
        "Select [bold green]Framework[/bold green]",
        choices=["Spring Boot", "Spring MVC", "Jakarta EE", "Plain Java"],
        default="Spring Boot"
    )
    return build_tool, framework

def get_dependencies():
    """Step 3: Dependencies."""
    console.print("\n[bold yellow]Step 3: Dependencies[/bold yellow]")
    
    # In a real CLI, we would use a checkbox list component (like inquirer.py).
    # Rich focuses on output, so we will simulate it with multiple prompts.
    
    deps = []
    if Confirm.ask("Add [bold cyan]Spring Web[/bold cyan]? (MVC/REST)", default=True):
        deps.append("web")
    if Confirm.ask("Add [bold cyan]Spring Security[/bold cyan]?", default=True):
        deps.append("security")
    if Confirm.ask("Add [bold cyan]Spring Data JPA[/bold cyan]? (Hibernate)", default=False):
        deps.append("jpa")
    if Confirm.ask("Add [bold cyan]Validation[/bold cyan]?", default=False):
        deps.append("validation")
    if Confirm.ask("Add [bold cyan]Lombok[/bold cyan]?", default=True):
        deps.append("lombok")
        
    return deps

def get_infrastructure():
    """Step 4: Infrastructure & Port."""
    console.print("\n[bold yellow]Step 4: Infrastructure & Runtime[/bold yellow]")
    
    db_type = Prompt.ask(
        "Select [bold green]Database[/bold green]",
        choices=["None", "PostgreSQL", "MySQL", "MongoDB", "H2 (In-memory)"],
        default="None"
    )
    
    cloud_provider = Prompt.ask(
        "Select [bold green]Cloud Infrastructure[/bold green]",
        choices=["None", "AWS", "GCP", "Azure"],
        default="None"
    )
    
    port = IntPrompt.ask("Application [bold green]Server Port[/bold green]", default=8080)
    
    return db_type, cloud_provider, port

def show_summary(group, artifact, version, build, framework, deps, db, cloud, port):
    """Step 5: Review & Generate."""
    console.print("\n[bold yellow]Step 5: Review & Generation[/bold yellow]")
    
    table = Table(title="Project Configuration Summary", border_style="cyan")
    table.add_column("Component", style="bold white")
    table.add_column("Configuration", style="green")
    
    table.add_row("Group ID", group)
    table.add_row("Artifact ID", artifact)
    table.add_row("Version", version)
    table.add_row("Build Tool", build)
    table.add_row("Framework", framework)
    table.add_row("Dependencies", ", ".join(deps) if deps else "None")
    table.add_row("Database", db)
    table.add_row("Cloud Provider", cloud)
    table.add_row("Server Port", str(port))
    
    console.print(table)
    
    if not Confirm.ask("\nProceed with generation?"):
        console.print("[bold red]Generation aborted by user.[/bold red]")
        return False
    return True

def generate_project(artifact_id):
    """Simulates project generation with a progress bar."""
    console.print("\n[bold cyan]Generating Project...[/bold cyan]")
    
    tasks = [
        ("Creating directory structure", 0.5),
        ("Generating build files", 1.0),
        ("Injecting dependencies", 0.8),
        ("Configuring application.yml", 0.6),
        ("Adding Docker assets", 1.2),
        ("Finalizing project", 0.4),
    ]
    
    with Progress(
        SpinnerColumn(),
        TextColumn("[progress.description]{task.description}"),
        transient=True,
    ) as progress:
        for task_desc, task_time in tasks:
            task = progress.add_task(description=task_desc, total=None)
            time.sleep(task_time)
            progress.update(task, completed=True)
            
    success_text = Text.assemble(
        ("\n✅ SUCCESS: ", "bold green"),
        ("Project '", "white"),
        (artifact_id, "bold cyan"),
        ("' has been initialized!", "white")
    )
    console.print(success_text)
    console.print(f"Run [bold white]cd {artifact_id}[/bold white] to begin.")

# --- Main Execution Flow ---
if __name__ == "__main__":
    show_welcome()
    
    # Get all configuration via wizard
    group, artifact, version = get_project_metadata()
    if not artifact: # Simple validation
        console.print("[bold red]Error: Artifact ID is mandatory.[/bold red]")
        exit(1)
        
    build, framework = get_core_stack()
    dependencies = get_dependencies()
    db, cloud, port = get_infrastructure()
    
    # Review Summary
    if show_summary(group, artifact, version, build, framework, dependencies, db, cloud, port):
        # Proceed with generation simulation
        generate_project(artifact)