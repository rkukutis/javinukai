function Button({ children, onClick, id, extraStyle }) {
  return (
    <button
      id={id}
      onClick={onClick}
      className={`bg-blue-400 hover:bg-blue-500 text-white py-2 px-4 rounded-md ${extraStyle}`}
    >
      {children}
    </button>
  );
}

export default Button;
