import { useState } from 'react';
import styles from './MessagesPage.module.css';

const CONVERSATIONS = [
  { id: 1, name: 'Nail Art Studio', initial: 'N', bg: '#f093fb', lastMsg: 'Your appointment is confirmed for Saturday!', time: '2h', unread: true, online: true },
  { id: 2, name: 'Hair by Linh', initial: 'H', bg: '#4facfe', lastMsg: 'Thank you! See you tomorrow at 2pm', time: '5h', unread: false, online: true },
  { id: 3, name: 'Makeup Artist Vy', initial: 'V', bg: '#a18cd1', lastMsg: 'Sent an attachment', time: '1d', unread: true, online: false },
  { id: 4, name: 'Lash by Trang', initial: 'T', bg: '#fcb69f', lastMsg: "We have a promotion this week!", time: '2d', unread: false, online: false },
  { id: 5, name: 'Spa Relax HCM', initial: 'S', bg: '#43e97b', lastMsg: 'Your feedback means a lot to us!', time: '3d', unread: false, online: false },
  { id: 6, name: 'Brow Master', initial: 'B', bg: '#667eea', lastMsg: 'New microblading technique available', time: '5d', unread: false, online: true },
  { id: 7, name: 'Skincare Daily', initial: 'S', bg: '#ff9a9e', lastMsg: 'Check out our new products!', time: '1w', unread: false, online: false },
];

const MESSAGES = [
  { id: 1, sender: 'them', text: 'Hi! Thanks for reaching out to Nail Art Studio', time: '10:30 AM' },
  { id: 2, sender: 'me', text: 'Hi! I saw your Galaxy Nail Art collection, it looks amazing!', time: '10:32 AM' },
  { id: 3, sender: 'them', text: 'Thank you so much! Would you like to book an appointment?', time: '10:33 AM' },
  { id: 4, sender: 'me', text: "Yes please! I'd like to get the Galaxy set. Do you have any availability this Saturday?", time: '10:35 AM' },
  { id: 5, sender: 'them', text: 'Let me check for you...', time: '10:37 AM' },
  { id: 6, sender: 'them', text: 'Yes! We have slots available at 10am, 2pm, and 4pm. Which time works best for you?', time: '10:38 AM' },
  { id: 7, sender: 'me', text: '2pm would be perfect!', time: '10:40 AM' },
  { id: 8, sender: 'them', text: 'Your appointment is confirmed for Saturday!', time: '10:41 AM' },
];

export default function MessagesPage() {
  const [activeConvo, setActiveConvo] = useState<number | null>(1);
  const [activeTab, setActiveTab] = useState('primary');
  const [inputValue, setInputValue] = useState('');

  const selected = CONVERSATIONS.find((c) => c.id === activeConvo);

  return (
    <div className={styles.container}>
      {/* Inbox */}
      <div className={styles.inbox}>
        <div className={styles.inboxHeader}>
          <div className={styles.inboxUser}>
            username
            <svg width="12" height="12" viewBox="0 0 24 24" fill="currentColor">
              <path d="M21 17.502a.997.997 0 0 1-.707-.293L12 8.913l-8.293 8.296a1 1 0 1 1-1.414-1.414l9-9.004a1.03 1.03 0 0 1 1.414 0l9 9.004A1 1 0 0 1 21 17.502Z" transform="rotate(180 12 12)" />
            </svg>
          </div>
          <button className={styles.newMsgBtn}>
            <svg width="24" height="24" viewBox="0 0 24 24" fill="currentColor">
              <path d="M12.202 3.203H5.25a3 3 0 0 0-3 3v10.5a3 3 0 0 0 3 3h10.5a3 3 0 0 0 3-3v-6.952" fill="none" stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" />
              <path d="M10.002 14.702 9.2 18.6l3.898-.8 8.679-8.678a1.5 1.5 0 0 0 0-2.121l-.879-.879a1.5 1.5 0 0 0-2.121 0Z" fill="none" stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" />
            </svg>
          </button>
        </div>

        <div className={styles.inboxTabs}>
          {(['primary', 'general', 'requests'] as const).map((tab) => (
            <button
              key={tab}
              className={activeTab === tab ? styles.inboxTabActive : styles.inboxTab}
              onClick={() => setActiveTab(tab)}
            >
              {tab.charAt(0).toUpperCase() + tab.slice(1)}
            </button>
          ))}
        </div>

        <div className={styles.conversations}>
          {CONVERSATIONS.map((c) => (
            <div
              key={c.id}
              className={activeConvo === c.id ? styles.convoItemActive : styles.convoItem}
              onClick={() => setActiveConvo(c.id)}
            >
              <div className={styles.convoAvatar} style={{ background: c.bg }}>
                {c.initial}
                {c.online && <span className={styles.onlineDot} />}
              </div>
              <div className={styles.convoInfo}>
                <div className={c.unread ? styles.convoNameUnread : styles.convoName}>{c.name}</div>
                <div className={styles.convoPreview}>
                  <span className={c.unread ? styles.convoLastMsgUnread : styles.convoLastMsg}>{c.lastMsg}</span>
                  <span className={styles.convoTime}> · {c.time}</span>
                </div>
              </div>
              {c.unread && <span className={styles.unreadDot} />}
            </div>
          ))}
        </div>
      </div>

      {/* Chat */}
      {selected ? (
        <div className={styles.chatArea}>
          <div className={styles.chatHeader}>
            <div className={styles.chatAvatar} style={{ background: selected.bg }}>
              {selected.initial}
            </div>
            <div className={styles.chatUserInfo}>
              <div className={styles.chatName}>{selected.name}</div>
              <div className={styles.chatStatus}>
                {selected.online ? 'Active now' : `Active ${selected.time} ago`}
              </div>
            </div>
            <div className={styles.chatHeaderActions}>
              <button className={styles.chatHeaderBtn}>
                <svg width="24" height="24" viewBox="0 0 24 24" fill="currentColor">
                  <rect fill="none" height="18" rx="3" stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" width="16.999" x="1" y="3" />
                  <line fill="none" stroke="currentColor" strokeLinejoin="round" strokeWidth="2" x1="6.003" x2="6.003" y1="21" y2="3" />
                </svg>
              </button>
              <button className={styles.chatHeaderBtn}>
                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                  <circle cx="12" cy="12" r="10" />
                  <line x1="12" y1="16" x2="12" y2="12" />
                  <line x1="12" y1="8" x2="12.01" y2="8" />
                </svg>
              </button>
            </div>
          </div>

          <div className={styles.chatMessages}>
            <div className={styles.dateLabel}>Today</div>
            {MESSAGES.map((msg, i) => {
              const isMe = msg.sender === 'me';
              const isLast = i === MESSAGES.length - 1;
              const nextDiffSender = i < MESSAGES.length - 1 && MESSAGES[i + 1].sender !== msg.sender;

              return (
                <div key={msg.id}>
                  <div className={isMe ? styles.msgRowSent : styles.msgRowReceived}>
                    {!isMe && (nextDiffSender || isLast) && (
                      <div className={styles.msgAvatar} style={{ background: selected.bg }}>
                        {selected.initial}
                      </div>
                    )}
                    {!isMe && !(nextDiffSender || isLast) && (
                      <div style={{ width: 28, flexShrink: 0 }} />
                    )}
                    <div className={isMe ? styles.bubbleSent : styles.bubbleReceived}>
                      {msg.text}
                    </div>
                  </div>
                  {(nextDiffSender || isLast) && (
                    <div className={isMe ? styles.msgTimeSent : styles.msgTime}>{msg.time}</div>
                  )}
                </div>
              );
            })}
            <div className={styles.seen}>Seen</div>
          </div>

          <div className={styles.chatInputBar}>
            <div className={styles.chatInputWrapper}>
              <button className={styles.emojiBtn}>
                <svg width="24" height="24" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M15.83 10.997a1.167 1.167 0 1 0 1.167 1.167 1.167 1.167 0 0 0-1.167-1.167Zm-6.5 1.167a1.167 1.167 0 1 0-1.166 1.167 1.167 1.167 0 0 0 1.166-1.167Zm5.163 3.24a3.406 3.406 0 0 1-4.982.007 1 1 0 1 0-1.557 1.256 5.397 5.397 0 0 0 8.09 0 1 1 0 0 0-1.55-1.263ZM12 .503a11.5 11.5 0 1 0 11.5 11.5A11.513 11.513 0 0 0 12 .503Zm0 21a9.5 9.5 0 1 1 9.5-9.5 9.51 9.51 0 0 1-9.5 9.5Z" />
                </svg>
              </button>
              <input
                className={styles.chatInput}
                placeholder="Message..."
                value={inputValue}
                onChange={(e) => setInputValue(e.target.value)}
              />
              {inputValue ? (
                <button className={styles.sendBtnVisible}>Send</button>
              ) : (
                <div className={styles.chatInputActions}>
                  <button className={styles.inputActionBtn}>
                    <svg width="24" height="24" viewBox="0 0 24 24" fill="currentColor">
                      <path d="M18.447 5.018a1.089 1.089 0 0 0-.879-.31l-5.116.742a.811.811 0 0 0-.122.022 4.893 4.893 0 0 0-3.66 3.12 2.76 2.76 0 0 1-1.607 1.761.81.81 0 0 0-.496.76v.138a.811.811 0 0 0 .811.811h1.893L7.834 17.5a.811.811 0 0 0 1.447.596l4.77-5.783h.136a.811.811 0 0 0 .81-.811v-.137a.809.809 0 0 0-.495-.76 2.752 2.752 0 0 1-1.243-1.043.815.815 0 0 0-.05-.076 4.877 4.877 0 0 0-.7-.762l4.31-.636a1.088 1.088 0 0 0 .628-1.07Z" fill="none" stroke="currentColor" strokeLinejoin="round" strokeWidth="2" />
                    </svg>
                  </button>
                  <button className={styles.inputActionBtn}>
                    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                      <rect x="3" y="3" width="18" height="18" rx="2" ry="2" />
                      <circle cx="8.5" cy="8.5" r="1.5" />
                      <polyline points="21,15 16,10 5,21" />
                    </svg>
                  </button>
                  <button className={styles.inputActionBtn}>
                    <svg width="24" height="24" viewBox="0 0 24 24" fill="currentColor">
                      <path d="M16.792 3.904A4.989 4.989 0 0 1 21.5 9.122c0 3.072-2.652 4.959-5.197 7.222-2.512 2.243-3.865 3.469-4.303 3.752-.477-.309-2.143-1.823-4.303-3.752C5.141 14.072 2.5 12.167 2.5 9.122a4.989 4.989 0 0 1 4.708-5.218 4.21 4.21 0 0 1 3.675 1.941c.84 1.175.98 1.763 1.12 1.763s.278-.588 1.11-1.766a4.17 4.17 0 0 1 3.679-1.938Z" fill="none" stroke="currentColor" strokeWidth="2" />
                    </svg>
                  </button>
                </div>
              )}
            </div>
          </div>
        </div>
      ) : (
        <div className={styles.chatArea}>
          <div className={styles.emptyState}>
            <div className={styles.emptyIcon}>
              <svg width="36" height="36" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
                <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z" />
              </svg>
            </div>
            <div className={styles.emptyTitle}>Your messages</div>
            <div className={styles.emptyDesc}>Send private messages to beauty artists</div>
            <button className={styles.emptyBtn}>Send message</button>
          </div>
        </div>
      )}
    </div>
  );
}
