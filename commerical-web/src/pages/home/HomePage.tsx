import { useState } from 'react';
import styles from './HomePage.module.css';

const STORIES = [
  { id: 1, name: 'your_story', initial: '+', seen: false },
  { id: 2, name: 'nailart_vn', initial: 'N', seen: false },
  { id: 3, name: 'hairby.mai', initial: 'H', seen: false },
  { id: 4, name: 'makeup_pro', initial: 'M', seen: true },
  { id: 5, name: 'lash.queen', initial: 'L', seen: false },
  { id: 6, name: 'skincare99', initial: 'S', seen: true },
  { id: 7, name: 'brow.artist', initial: 'B', seen: false },
  { id: 8, name: 'spa.relax', initial: 'R', seen: true },
  { id: 9, name: 'tattoo.sg', initial: 'T', seen: false },
];

const POSTS = [
  {
    id: 1, username: 'nailart_studio', initial: 'N', verified: true,
    time: '8h', gradient: 'linear-gradient(135deg, #f093fb, #f5576c)',
    likes: 1243, likedBy: 'hairby.linh',
    caption: 'Galaxy Nail Art collection has arrived! Swipe to see all designs. Limited slots available for this week',
    tags: '#nailart #galaxynails #beautygram',
    comments: [
      { user: 'makeup.vy', text: 'So gorgeous!! Need this' },
      { user: 'lash.trang', text: 'Booking now!!' },
    ],
    totalComments: 48,
  },
  {
    id: 2, username: 'hairby.linh', initial: 'H', verified: true,
    time: '12h', gradient: 'linear-gradient(135deg, #4facfe, #00f2fe)',
    likes: 892, likedBy: 'nailart_studio',
    caption: 'Balayage transformation. Before and after! DM to book your appointment',
    tags: '#balayage #hairtransformation #saigonhair',
    comments: [
      { user: 'beauty_spa', text: 'Amazing work as always!' },
    ],
    totalComments: 32,
  },
  {
    id: 3, username: 'makeup.artist.vy', initial: 'V', verified: false,
    time: '1d', gradient: 'linear-gradient(135deg, #a18cd1, #fbc2eb)',
    likes: 2103, likedBy: 'hairby.linh',
    caption: 'Bridal makeup look for today. Congratulations to the beautiful couple!',
    tags: '#bridalmakeup #weddingmakeup #beautygram',
    comments: [
      { user: 'skincare.daily', text: 'You never miss' },
      { user: 'brow.master', text: 'The glow is real' },
    ],
    totalComments: 87,
  },
  {
    id: 4, username: 'lash.by.trang', initial: 'T', verified: false,
    time: '2d', gradient: 'linear-gradient(135deg, #ffecd2, #fcb69f)',
    likes: 567, likedBy: 'makeup.artist.vy',
    caption: 'Classic lash set. Natural and elegant, perfect for everyday wear',
    tags: '#classiclash #lashextensions',
    comments: [
      { user: 'nailart_studio', text: 'Love the natural look!' },
    ],
    totalComments: 21,
  },
];

const SUGGESTIONS = [
  { id: 1, name: 'beauty_spa_hcm', initial: 'B', reason: 'Followed by hairby.linh + 3 more' },
  { id: 2, name: 'tattoo.art.sg', initial: 'T', reason: 'Suggested for you' },
  { id: 3, name: 'skincare.daily', initial: 'S', reason: 'Followed by nailart_studio' },
  { id: 4, name: 'brow.master.vn', initial: 'B', reason: 'Suggested for you' },
  { id: 5, name: 'waxing.expert', initial: 'W', reason: 'New to Beautygram' },
];

export default function HomePage() {
  const [liked, setLiked] = useState<Record<number, boolean>>({});
  const [saved, setSaved] = useState<Record<number, boolean>>({});

  return (
    <div className={styles.feedLayout}>
      <div className={styles.feedMain}>
        {/* Stories */}
        <div className={styles.stories}>
          {STORIES.map((s) => (
            <div key={s.id} className={styles.storyItem}>
              <div className={s.seen ? styles.storyRingSeen : styles.storyRing}>
                <div className={styles.storyAvatar}>{s.initial}</div>
              </div>
              <span className={s.seen ? styles.storyNameSeen : styles.storyName}>{s.name}</span>
            </div>
          ))}
        </div>

        {/* Posts */}
        {POSTS.map((post) => (
          <article key={post.id} className={styles.post}>
            {/* Header */}
            <div className={styles.postHeader}>
              <div className={styles.postAvatarWrap}>
                <div className={styles.postAvatar}>{post.initial}</div>
              </div>
              <div className={styles.postUserInfo}>
                <span className={styles.postUsername}>{post.username}</span>
                {post.verified && (
                  <span className={styles.postVerified}>
                    <VerifiedIcon />
                  </span>
                )}
                <span className={styles.postDot}>•</span>
                <span className={styles.postTime}>{post.time}</span>
              </div>
              <button className={styles.postMoreBtn}>
                <MoreHorizIcon />
              </button>
            </div>

            {/* Image */}
            <div className={styles.postImage}>
              <div className={styles.postImagePlaceholder} style={{ background: post.gradient }}>
                <ImagePlaceholderIcon />
              </div>
            </div>

            {/* Actions */}
            <div className={styles.postActions}>
              <button
                className={liked[post.id] ? styles.likeActive : styles.actionBtn}
                onClick={() => setLiked((p) => ({ ...p, [post.id]: !p[post.id] }))}
              >
                {liked[post.id] ? <HeartFilledIcon /> : <HeartIcon />}
              </button>
              <button className={styles.actionBtn}><CommentIcon /></button>
              <button className={styles.actionBtn}><ShareIcon /></button>
              <button
                className={styles.bookmarkBtn}
                onClick={() => setSaved((p) => ({ ...p, [post.id]: !p[post.id] }))}
              >
                {saved[post.id] ? <BookmarkFilledIcon /> : <BookmarkIcon />}
              </button>
            </div>

            {/* Likes */}
            <div className={styles.postLikes}>
              {liked[post.id] ? (post.likes + 1).toLocaleString() : post.likes.toLocaleString()} likes
            </div>

            {/* Caption */}
            <div className={styles.postCaption}>
              <span className={styles.captionUser}>{post.username}</span>
              {post.caption}{' '}
              <span className={styles.captionMore}>{post.tags}</span>
            </div>

            {/* Comments preview */}
            {post.totalComments > 2 && (
              <div className={styles.viewComments}>
                View all {post.totalComments} comments
              </div>
            )}
            {post.comments.map((c, i) => (
              <div key={i} className={styles.commentPreview}>
                <span className={styles.commentUser}>{c.user}</span>
                <span>{c.text}</span>
              </div>
            ))}

            {/* Add comment */}
            <div className={styles.addComment}>
              <button className={styles.emojiBtn}><EmojiIcon /></button>
              <input className={styles.addCommentInput} placeholder="Add a comment..." />
            </div>
          </article>
        ))}
      </div>

      {/* Sidebar */}
      <aside className={styles.feedSidebar}>
        <div className={styles.sideProfile}>
          <div className={styles.sideAvatarWrap}>
            <div className={styles.sideAvatar}>U</div>
          </div>
          <div className={styles.sideUserInfo}>
            <div className={styles.sideUsername}>username</div>
            <div className={styles.sideFullname}>Your Name</div>
          </div>
          <button className={styles.switchBtn}>Switch</button>
        </div>

        <div className={styles.suggestHeader}>
          <span className={styles.suggestTitle}>Suggested for you</span>
          <button className={styles.seeAllBtn}>See All</button>
        </div>

        {SUGGESTIONS.map((s) => (
          <div key={s.id} className={styles.suggestItem}>
            <div className={styles.suggestAvatarWrap}>
              <div className={styles.suggestAvatar}>{s.initial}</div>
            </div>
            <div className={styles.suggestInfo}>
              <div className={styles.suggestName}>{s.name}</div>
              <div className={styles.suggestReason}>{s.reason}</div>
            </div>
            <button className={styles.followBtn}>Follow</button>
          </div>
        ))}

        <div className={styles.footer}>
          <span className={styles.footerLink}>About</span>
          <span className={styles.footerLink}>Help</span>
          <span className={styles.footerLink}>Press</span>
          <span className={styles.footerLink}>API</span>
          <span className={styles.footerLink}>Jobs</span>
          <span className={styles.footerLink}>Privacy</span>
          <span className={styles.footerLink}>Terms</span>
          <span className={styles.footerLink}>Locations</span>
          <span className={styles.footerLink}>Language</span>
        </div>
        <div className={styles.copyright}>@ 2026 Beautygram</div>
      </aside>
    </div>
  );
}

/* ===== Icons ===== */
function VerifiedIcon() {
  return (
    <svg aria-label="Verified" fill="#0095f6" height="12" viewBox="0 0 40 40" width="12">
      <path d="M19.998 3.094 14.638 0l-2.972 5.15H5.432v6.354L0 14.64 3.094 20 0 25.359l5.432 3.137v5.905h5.975L14.638 40l5.36-3.094L25.358 40l3.232-5.6h6.162v-6.01L40 25.359 36.905 20 40 14.641l-5.248-3.03v-6.46h-6.419L25.358 0l-5.36 3.094Zm7.415 11.225 2.254 2.287-11.43 11.5-6.835-6.93 2.244-2.258 4.587 4.581 9.18-9.18Z" fillRule="evenodd" />
    </svg>
  );
}

function MoreHorizIcon() {
  return (
    <svg aria-label="More options" fill="currentColor" height="24" viewBox="0 0 24 24" width="24">
      <circle cx="12" cy="12" r="1.5" /><circle cx="6" cy="12" r="1.5" /><circle cx="18" cy="12" r="1.5" />
    </svg>
  );
}

function ImagePlaceholderIcon() {
  return (
    <svg width="64" height="64" viewBox="0 0 24 24" fill="currentColor" opacity="0.15">
      <path d="M21 19V5c0-1.1-.9-2-2-2H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2zM8.5 13.5l2.5 3.01L14.5 12l4.5 6H5l3.5-4.5z" />
    </svg>
  );
}

function HeartIcon() {
  return (
    <svg aria-label="Like" fill="currentColor" height="24" viewBox="0 0 24 24" width="24">
      <path d="M16.792 3.904A4.989 4.989 0 0 1 21.5 9.122c0 3.072-2.652 4.959-5.197 7.222-2.512 2.243-3.865 3.469-4.303 3.752-.477-.309-2.143-1.823-4.303-3.752C5.141 14.072 2.5 12.167 2.5 9.122a4.989 4.989 0 0 1 4.708-5.218 4.21 4.21 0 0 1 3.675 1.941c.84 1.175.98 1.763 1.12 1.763s.278-.588 1.11-1.766a4.17 4.17 0 0 1 3.679-1.938Z" fill="none" stroke="currentColor" strokeWidth="2" />
    </svg>
  );
}

function HeartFilledIcon() {
  return (
    <svg aria-label="Unlike" fill="#ff3040" height="24" viewBox="0 0 48 48" width="24">
      <path d="M34.6 3.1c-4.5 0-7.9 1.8-10.6 5.6-2.7-3.7-6.1-5.5-10.6-5.5C6 3.1 0 9.6 0 17.6c0 7.3 5.4 12 10.6 16.5.6.5 1.3 1.1 1.9 1.7l2.3 2c4.4 3.9 6.6 5.9 7.6 6.5.5.3 1.1.5 1.6.5s1.1-.2 1.6-.5c1-.6 2.8-2.2 7.8-6.8l2-1.8c.7-.6 1.3-1.2 2-1.7C42.7 29.6 48 25 48 17.6c0-8-6-14.5-13.4-14.5z" />
    </svg>
  );
}

function CommentIcon() {
  return (
    <svg aria-label="Comment" fill="currentColor" height="24" viewBox="0 0 24 24" width="24">
      <path d="M20.656 17.008a9.993 9.993 0 1 0-3.59 3.615L22 22Z" fill="none" stroke="currentColor" strokeLinejoin="round" strokeWidth="2" />
    </svg>
  );
}

function ShareIcon() {
  return (
    <svg aria-label="Share Post" fill="currentColor" height="24" viewBox="0 0 24 24" width="24">
      <line fill="none" stroke="currentColor" strokeLinejoin="round" strokeWidth="2" x1="22" x2="9.218" y1="3" y2="10.083" />
      <polygon fill="none" points="11.698 20.334 22 3.001 2 3.001 9.218 10.084 11.698 20.334" stroke="currentColor" strokeLinejoin="round" strokeWidth="2" />
    </svg>
  );
}

function BookmarkIcon() {
  return (
    <svg aria-label="Save" fill="currentColor" height="24" viewBox="0 0 24 24" width="24">
      <polygon fill="none" points="20 21 12 13.44 4 21 4 3 20 3 20 21" stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" />
    </svg>
  );
}

function BookmarkFilledIcon() {
  return (
    <svg aria-label="Remove" fill="currentColor" height="24" viewBox="0 0 24 24" width="24">
      <polygon points="20 21 12 13.44 4 21 4 3 20 3 20 21" />
    </svg>
  );
}

function EmojiIcon() {
  return (
    <svg aria-label="Emoji" fill="currentColor" height="13" viewBox="0 0 24 24" width="13">
      <path d="M15.83 10.997a1.167 1.167 0 1 0 1.167 1.167 1.167 1.167 0 0 0-1.167-1.167Zm-6.5 1.167a1.167 1.167 0 1 0-1.166 1.167 1.167 1.167 0 0 0 1.166-1.167Zm5.163 3.24a3.406 3.406 0 0 1-4.982.007 1 1 0 1 0-1.557 1.256 5.397 5.397 0 0 0 8.09 0 1 1 0 0 0-1.55-1.263ZM12 .503a11.5 11.5 0 1 0 11.5 11.5A11.513 11.513 0 0 0 12 .503Zm0 21a9.5 9.5 0 1 1 9.5-9.5 9.51 9.51 0 0 1-9.5 9.5Z" />
    </svg>
  );
}
