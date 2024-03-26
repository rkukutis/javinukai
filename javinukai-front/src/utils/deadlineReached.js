export default function (deadline) {
  let date1 = new Date().getTime();
  let date2 = new Date(deadline).getTime();
  const res = date1 >= date2;
  return res;
}
