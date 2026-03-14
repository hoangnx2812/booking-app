import { useState } from 'react';
import styles from './ProfilePage.module.css';

const HIGHLIGHTS = [
  { id: 0, name: 'New', icon: '+', isAdd: true },
  { id: 1, name: 'Nails', icon: '💅' },
  { id: 2, name: 'Hair', icon: '💇' },
  { id: 3, name: 'Reviews', icon: '⭐' },
  { id: 4, name: 'Studio', icon: '🏠' },
  { id: 5, name: 'Promos', icon: '🎉' },
  { id: 6, name: 'Before/After', icon: '✨' },
];

const GRADIENTS = [
  'linear-gradient(135deg, #f093fb, #f5576c)',
  'linear-gradient(135deg, #4facfe, #00f2fe)',
  'linear-gradient(135deg, #a18cd1, #fbc2eb)',
  'linear-gradient(135deg, #ffecd2, #fcb69f)',
  'linear-gradient(135deg, #667eea, #764ba2)',
  'linear-gradient(135deg, #ff9a9e, #fecfef)',
  'linear-gradient(135deg, #43e97b, #38f9d7)',
  'linear-gradient(135deg, #fa709a, #fee140)',
  'linear-gradient(135deg, #a1c4fd, #c2e9fb)',
  'linear-gradient(135deg, #30cfd0, #330867)',
  'linear-gradient(135deg, #d4fc79, #96e6a1)',
  'linear-gradient(135deg, #f6d365, #fda085)',
];

const POSTS = Array.from({ length: 18 }, (_, i) => ({
  id: i + 1,
  gradient: GRADIENTS[i % GRADIENTS.length],
  likes: Math.floor(Math.random() * 5000) + 100,
  comments: Math.floor(Math.random() * 200) + 5,
  isMulti: i % 5 === 0,
}));

type TabKey = 'posts' | 'reels' | 'saved' | 'tagged';

const TABS: { key: TabKey; label: string; icon: () => React.ReactNode }[] = [
  { key: 'posts', label: 'Posts', icon: GridIcon },
  { key: 'reels', label: 'Reels', icon: ReelsTabIcon },
  { key: 'saved', label: 'Saved', icon: SavedTabIcon },
  { key: 'tagged', label: 'Tagged', icon: TaggedTabIcon },
];

export default function ProfilePage() {
  const [activeTab, setActiveTab] = useState<TabKey>('posts');

  return (
    <div className={styles.container}>
      {/* Header */}
      <div className={styles.header}>
        <div className={styles.avatarCol}>
          <div className={styles.avatarRing}>
            <div className={styles.avatar}>U</div>
          </div>
        </div>

        <div className={styles.infoCol}>
          <div className={styles.row1}>
            <span className={styles.username}>username</span>
            <button className={styles.profileBtn}>Edit profile</button>
            <button className={styles.profileBtn}>View archive</button>
            <button className={styles.iconBtn}>
              <SettingsIcon />
            </button>
          </div>

          <div className={styles.stats}>
            <span className={styles.stat}><span className={styles.statNum}>127</span> posts</span>
            <span className={styles.stat}><span className={styles.statNum}>3,456</span> followers</span>
            <span className={styles.stat}><span className={styles.statNum}>892</span> following</span>
          </div>

          <div className={styles.category}>Beauty Artist</div>
          <div className={styles.displayName}>Your Name</div>
          <div className={styles.bio}>
            {'✨ Beauty enthusiast & professional artist\n📍 Ho Chi Minh City\n📅 Book appointments via DM\n🔗 '}
            <span className={styles.bioLink}>beautygram.com/username</span>
          </div>
          <div className={styles.followedBy}>
            Followed by <span className={styles.followedByLink}>hairby.linh</span>,{' '}
            <span className={styles.followedByLink}>nailart_studio</span>, and{' '}
            <span className={styles.followedByLink}>23 others</span>
          </div>
        </div>
      </div>

      {/* Highlights */}
      <div className={styles.highlights}>
        {HIGHLIGHTS.map((h) => (
          <div key={h.id} className={styles.highlight}>
            <div className={h.isAdd ? styles.highlightAdd : styles.highlightCircle}>
              {h.icon}
            </div>
            <span className={styles.highlightLabel}>{h.name}</span>
          </div>
        ))}
      </div>

      {/* Tabs */}
      <div className={styles.tabBar}>
        {TABS.map((tab) => (
          <button
            key={tab.key}
            className={activeTab === tab.key ? styles.tabItemActive : styles.tabItem}
            onClick={() => setActiveTab(tab.key)}
          >
            <tab.icon />
            {tab.label}
          </button>
        ))}
      </div>

      {/* Grid / Empty states */}
      {activeTab === 'posts' && (
        <div className={styles.postGrid}>
          {POSTS.map((post) => (
            <div key={post.id} className={styles.gridItem}>
              <div className={styles.gridItemContent} style={{ background: post.gradient }}>
                <svg width="32" height="32" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M21 19V5c0-1.1-.9-2-2-2H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2zM8.5 13.5l2.5 3.01L14.5 12l4.5 6H5l3.5-4.5z" />
                </svg>
              </div>
              {post.isMulti && (
                <span className={styles.gridMulti}>
                  <svg width="16" height="16" viewBox="0 0 48 48" fill="white">
                    <path d="M34.8 29.7V11c0-2.9-2.3-5.2-5.2-5.2H11c-2.9 0-5.2 2.3-5.2 5.2v18.7c0 2.9 2.3 5.2 5.2 5.2h18.7c2.8-.1 5.1-2.4 5.1-5.2zM39.2 15v16.1c0 4.5-3.7 8.2-8.2 8.2H14.9c-.6 0-.9.7-.5 1.1 1 1.1 2.4 1.8 4.1 1.8h13.4c5.7 0 10.3-4.6 10.3-10.3V18.5c0-1.6-.7-3.1-1.8-4.1-.5-.4-1.2 0-1.2.6z" />
                  </svg>
                </span>
              )}
              <div className={styles.gridOverlay}>
                <span className={styles.gridStat}>
                  <svg width="18" height="18" viewBox="0 0 24 24" fill="white">
                    <path d="M16.792 3.904A4.989 4.989 0 0 1 21.5 9.122c0 3.072-2.652 4.959-5.197 7.222-2.512 2.243-3.865 3.469-4.303 3.752-.477-.309-2.143-1.823-4.303-3.752C5.141 14.072 2.5 12.167 2.5 9.122a4.989 4.989 0 0 1 4.708-5.218 4.21 4.21 0 0 1 3.675 1.941c.84 1.175.98 1.763 1.12 1.763s.278-.588 1.11-1.766a4.17 4.17 0 0 1 3.679-1.938Z" />
                  </svg>
                  {post.likes >= 1000 ? `${(post.likes / 1000).toFixed(1)}k` : post.likes}
                </span>
                <span className={styles.gridStat}>
                  <svg width="18" height="18" viewBox="0 0 24 24" fill="white">
                    <path d="M20.656 17.008a9.993 9.993 0 1 0-3.59 3.615L22 22Z" />
                  </svg>
                  {post.comments}
                </span>
              </div>
            </div>
          ))}
        </div>
      )}

      {activeTab === 'reels' && (
        <div className={styles.emptyTab}>
          <div className={styles.emptyTabIcon}>
            <svg width="26" height="26" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5">
              <rect x="2" y="2" width="20" height="20" rx="4" />
              <path d="M9.5 8.5 16 12l-6.5 3.5z" />
            </svg>
          </div>
          <div className={styles.emptyTabTitle}>Share reels</div>
          <div className={styles.emptyTabDesc}>Create and share short videos to showcase your work.</div>
        </div>
      )}

      {activeTab === 'saved' && (
        <div className={styles.emptyTab}>
          <div className={styles.emptyTabIcon}>
            <svg width="26" height="26" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5">
              <polygon points="20,21 12,13.44 4,21 4,3 20,3 20,21" />
            </svg>
          </div>
          <div className={styles.emptyTabTitle}>Save</div>
          <div className={styles.emptyTabDesc}>Save photos and videos that you want to see again. Only you can see what you've saved.</div>
        </div>
      )}

      {activeTab === 'tagged' && (
        <div className={styles.emptyTab}>
          <div className={styles.emptyTabIcon}>
            <svg width="26" height="26" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
              <circle cx="12" cy="8" r="4" />
              <path d="M20 21a8 8 0 1 0-16 0" />
            </svg>
          </div>
          <div className={styles.emptyTabTitle}>Photos of you</div>
          <div className={styles.emptyTabDesc}>When people tag you in photos, they'll appear here.</div>
        </div>
      )}
    </div>
  );
}

/* ===== Tab Icons ===== */
function GridIcon() {
  return (
    <svg aria-label="Posts" fill="currentColor" height="12" viewBox="0 0 24 24" width="12">
      <rect fill="none" height="18" stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" width="18" x="3" y="3" />
      <line fill="none" stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" x1="9.015" x2="9.015" y1="3" y2="21" />
      <line fill="none" stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" x1="14.985" x2="14.985" y1="3" y2="21" />
      <line fill="none" stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" x1="21" x2="3" y1="9.015" y2="9.015" />
      <line fill="none" stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" x1="21" x2="3" y1="14.985" y2="14.985" />
    </svg>
  );
}

function ReelsTabIcon() {
  return (
    <svg aria-label="Reels" fill="currentColor" height="12" viewBox="0 0 24 24" width="12">
      <line fill="none" stroke="currentColor" strokeLinejoin="round" strokeWidth="2" x1="2.049" x2="21.95" y1="7.002" y2="7.002" />
      <line fill="none" stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" x1="13.504" x2="16.362" y1="2.001" y2="7.002" />
      <line fill="none" stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" x1="7.207" x2="10.002" y1="2.11" y2="7.002" />
      <path d="M2 12.001v3.449c0 2.849.698 4.006 1.606 4.945.94.983 2.168 1.587 4.443 1.587h7.935c2.276 0 3.503-.604 4.444-1.587.908-.939 1.605-2.096 1.605-4.945V12" fill="none" stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" />
      <path d="M9.763 17.664a.908.908 0 0 1-.454-.787V11.63a.909.909 0 0 1 1.364-.788l4.631 2.6a.91.91 0 0 1 0 1.575l-4.63 2.6a.91.91 0 0 1-.911.047Z" fill="none" stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" />
    </svg>
  );
}

function SavedTabIcon() {
  return (
    <svg aria-label="Saved" fill="currentColor" height="12" viewBox="0 0 24 24" width="12">
      <polygon fill="none" points="20,21 12,13.44 4,21 4,3 20,3 20,21" stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" />
    </svg>
  );
}

function TaggedTabIcon() {
  return (
    <svg aria-label="Tagged" fill="currentColor" height="12" viewBox="0 0 24 24" width="12">
      <path d="M10.201 3.797 12 1.997l1.799 1.8a1.59 1.59 0 0 0 1.124.465h2.547a1.536 1.536 0 0 1 1.535 1.536v2.547a1.59 1.59 0 0 0 .465 1.124l1.8 1.799-1.8 1.799a1.59 1.59 0 0 0-.465 1.124v2.547a1.536 1.536 0 0 1-1.535 1.536h-2.547a1.59 1.59 0 0 0-1.124.465l-1.799 1.8-1.799-1.8a1.59 1.59 0 0 0-1.124-.465H6.53a1.536 1.536 0 0 1-1.535-1.536v-2.547a1.59 1.59 0 0 0-.465-1.124l-1.8-1.799 1.8-1.799a1.59 1.59 0 0 0 .465-1.124V6.798A1.536 1.536 0 0 1 6.53 5.262h2.547a1.59 1.59 0 0 0 1.124-.465Z" fill="none" stroke="currentColor" strokeLinejoin="round" strokeWidth="2" />
      <path d="M12 15a3 3 0 1 0 0-6 3 3 0 0 0 0 6Z" fill="none" stroke="currentColor" strokeMiterlimit="10" strokeWidth="2" />
    </svg>
  );
}

function SettingsIcon() {
  return (
    <svg aria-label="Options" fill="currentColor" height="24" viewBox="0 0 24 24" width="24">
      <circle cx="12" cy="12" fill="none" r="8.635" stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" />
      <path d="M14.232 3.656a1.269 1.269 0 0 1-.796-.66L12.93 2h-1.86l-.505.996a1.269 1.269 0 0 1-.796.66m-.001 16.688a1.269 1.269 0 0 1 .796.66l.505.996h1.862l.505-.996a1.269 1.269 0 0 1 .796-.66M3.656 9.768a1.269 1.269 0 0 1-.66.796L2 11.07v1.862l.996.505a1.269 1.269 0 0 1 .66.796m16.688-.001a1.269 1.269 0 0 1 .66-.796L22 12.93v-1.86l-.996-.505a1.269 1.269 0 0 1-.66-.796M7.678 4.522a1.269 1.269 0 0 1-1.03.096l-1.06-.348L4.27 5.587l.348 1.062a1.269 1.269 0 0 1-.096 1.03m11.8 9.122a1.269 1.269 0 0 1 1.03-.096l1.06.348 1.318-1.317-.349-1.062a1.269 1.269 0 0 1 .096-1.03m-14.956.001a1.269 1.269 0 0 1 .096 1.03l-.348 1.06 1.317 1.318 1.062-.349a1.269 1.269 0 0 1 1.03.096m9.122-11.8a1.269 1.269 0 0 1-.096-1.03l.348-1.06-1.317-1.318-1.062.349a1.269 1.269 0 0 1-1.03-.096" fill="none" stroke="currentColor" strokeLinejoin="round" strokeWidth="2" />
    </svg>
  );
}
