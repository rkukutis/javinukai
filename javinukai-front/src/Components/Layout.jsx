import { Outlet } from "react-router-dom";
import LikeButton from "./LikeButton";
import NavBar from "./NavBar";
import Button from "./Button";

function Layout() {
  return (
    <div>
      <header className="text h-[12vh] bg-slate-700 text-slate-100">
        <NavBar />
        <LikeButton />
        <Button/>
      </header>
      <main className="min-h-[82vh]">
        <Outlet />
      </main>
      <footer className="h-[10vh] bg-slate-700 text-slate-100">FOOTER</footer>
    </div>
  );
}

export default Layout;
