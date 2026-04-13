import { useEffect, useState } from "react";
import { ThemeProvider } from "./contexts/Theme";
import ThemeBtn from "./components/ThemeBtn";
import Card from "./components/Card";

function App() {
  const [themeMode, setThemeMode] = useState("light");

  const lightTheme = () => {
    setThemeMode("light");
  };

  const darkTheme = () => {
    setThemeMode("dark");
  };

  useEffect(() => {
    const root = document.documentElement;
    root.classList.remove("light", "dark");
    root.classList.add(themeMode);
  }, [themeMode]);

  return (
    <>
      <ThemeProvider value={{ themeMode, lightTheme, darkTheme }}>
        <div className="flex min-h-screen flex-wrap items-center bg-gray-100 text-gray-900 transition-colors duration-300 dark:bg-gray-900 dark:text-white">
          <div className="w-full">
            <div className="w-full max-w-sm mx-auto flex justify-end mb-4">
              {/*themeButton*/}
              <ThemeBtn />
            </div>

            <div className="w-full max-w-sm mx-auto"></div>
            {/*Card*/}
            <Card />
          </div>
        </div>
      </ThemeProvider>
    </>
  );
}

export default App;
