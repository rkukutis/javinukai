export default function SeriesPhotoCollection({ photo, onClick }) {
  return (
    <img
      onClick={onClick}
      className="rounded hover:scale-105 hover:cursor-pointer"
      key={photo.id}
      src={photo.urlSmall}
    />
  );
}
