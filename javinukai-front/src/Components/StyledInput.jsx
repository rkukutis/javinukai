function StyledInput({ value, type, id, extraStyle, disabled }) {
  return (
    <input
      id={id}
      disabled={disabled}
      className={
        "bg-blue-500 py-1 curso text-slate-50 rounded hover:cursor-pointer " +
        extraStyle
      }
      value={value}
      type={type}
    />
  );
}

export default StyledInput;
