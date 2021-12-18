package fr.cobaldhub.utils.scoreboard;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public abstract class SidebarManager {
	
	HashMap<UUID, Sidebar> sidebars = new HashMap<>();
	HashMap<UUID, Sideline> sidelines = new HashMap<>();
	
	@SuppressWarnings("unused")
	private TimeProvider timeProvider = null;

	public synchronized void update() {
		for (Map.Entry<UUID, Sidebar> e : sidebars.entrySet()) {
			UUID p = e.getKey();
			Sidebar sb = e.getValue();
			Sideline sl = sidelines.get(e.getKey());
			
			SidebarEditor editor = new SidebarEditor(sb, sl, "DefaultTitle");
			build(p, editor);
			editor.send();
		}
	}

	@Deprecated
	public void gameSec(Integer time) {
		update();
	}

	@Deprecated
	public void gameUpdate() {
		update();
	}

	@Deprecated
	public void ingame(Sidebar sb, Sideline sl, UUID p) {}

	@Deprecated
	public void finished(Sidebar sb, Sideline sl, UUID p) {}
	
	public void build(UUID p, SidebarEditor e) {}
	
	public void join(UUID p) {
		Sidebar sb = new Sidebar(p);
		sidebars.put(p, sb);
		sidelines.put(p, new Sideline(sb));
	}
	
	public void leave(UUID p) {
		Sidebar sb = sidebars.remove(p);
		if (sb != null) {
			sb.remove();
		}
		sidelines.remove(p);
	}
	
	public class SidebarEditor {
		Sidebar sb;
		Sideline sl;
		String title;
		Boolean flush = false;
		
		SidebarEditor(Sidebar sb, Sideline sl, String defaultTitle) {
			this.sb = sb;
			this.sl = sl;
			this.title = defaultTitle;
		}

		public void add(String str) {
			sl.add(str);
			flush = true;
		}

		public void setAt(Integer i, String str) {
			sl.set(i, str);
		}

		public void setByScore(String str, Integer score) {
			sb.setLine(str, score);
		}

		public void clear() {
			sl.clear();
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public Integer getRemainingSize() {
			return sl.getRemainingSize();
		}
		
		void send() {
			if (flush)
				sl.flush();
			else
				sb.send();
			sb.setName(title);
		}
	}

	public void setTimeProvider(TimeProvider timeProvider) {
		this.timeProvider = timeProvider;
	}

	public static interface TimeProvider {

		public Integer getTime();
	}
}