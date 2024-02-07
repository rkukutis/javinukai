function FormFieldError({ children }) {
  return (
    <p className="bg-red-200 rounded-sm text-red-700 py-1 px-2 mt-2">
      {children}
    </p>
  );
}

export default FormFieldError;
