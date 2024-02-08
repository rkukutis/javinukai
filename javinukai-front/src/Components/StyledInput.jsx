function StyledInput({ value, type, id }) {
  return (
    <input
      id={id}
      className="bg-blue-500 py-1 curso text-slate-50 rounded-sm hover:cursor-pointer"
      value={value}
      type={type}
    />
  );
}

export default StyledInput;
