export default function (email) {
  const regex = new RegExp(/^[\w-.]+@([\w-]+\.)+[\w-]{2,4}$/);
  return regex.test(email);
}
