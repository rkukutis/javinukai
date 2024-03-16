function StyledInput({ value, type, id, extraStyle, disabled, form }) {
  return (
    <input
      form={form}
      id={id}
      disabled={disabled}
      className={
        "bg-blue-500 hover:bg-blue-400 transition py-2 curso text-white rounded hover:cursor-pointer " +
        extraStyle
      }
      value={value}
      type={type}
    />
  );
}

export default StyledInput;
