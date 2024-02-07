import { Outlet } from "react-router-dom";
import NavBar from "./NavBar";

function Layout() {
  return (
    <div>
      <header className="text h-[8vh] bg-slate-700 text-slate-100">
        <NavBar />
      </header>
      <main className="min-h-[82vh]">
        <Outlet />
      </main>
      <footer className="h-[10vh] bg-slate-700 text-slate-100">FOOTER</footer>
    </div>
  );
}

export default Layout;
