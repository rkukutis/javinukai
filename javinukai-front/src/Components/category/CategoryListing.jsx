export default function CategoryListing({ categoryInfo }) {
  return (
    <div className="py-1 bg-white mt-1 rounded-md px-2 text-slate-700">
      <p className="truncate">{categoryInfo.name}</p>
    </div>
  );
}
