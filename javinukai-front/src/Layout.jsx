import { Outlet } from "react-router-dom";

function Layout() {
  return (
    <div>
      <header className="text h-[8vh] bg-slate-700 text-slate-100">
        HEADER
      </header>
      <main className="text min-h-[82vh]">
        <Outlet />
      </main>
      <footer className="h-[10vh] bg-slate-700">FOOTER</footer>
    </div>
  );
}

export default Layout;
