export default function (dateString) {
  const date = new Date(dateString);
  return date.toLocaleDateString("lt-lt");
}
