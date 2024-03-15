function PageContent({ children }) {
  return (
    <div className="w-full min-h-[82vh] flex flex-col items-center bg-slate-50">
      {children}
    </div>
  );
}

export default PageContent;
