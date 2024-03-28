function Button({ children, onClick, id, extraStyle, disabled, type }) {
  return (
    <button
      type={type ? type : "button"}
      disabled={disabled}
      id={id}
      onClick={onClick}
      className={`bg-blue-500 hover:bg-blue-400 ${
        disabled ? "bg-blue-300 cursor-not-allowed hover:bg-blue-300" : ""
      } text-white py-2 px-4 rounded-md transition ease-in-out ${extraStyle}`}
    >
      {children}
    </button>
  );
}

export default Button;
